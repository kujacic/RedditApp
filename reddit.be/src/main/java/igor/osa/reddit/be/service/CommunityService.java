package igor.osa.reddit.be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import igor.osa.reddit.be.model.*;
import igor.osa.reddit.be.repository.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.CommunityDTO;

@Service
public class CommunityService {
	
	private static final Logger LOGGER = LogManager.getLogger(CommunityService.class);
	
	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private BannedRepository bannedRepository;
	
	@Autowired 
	private RuleRepository ruleRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ModeratorRepository moderatorRepository;
	
	@Autowired
	private FlairRepository flairRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	public List<CommunityDTO> getAll() {
		List<Community> communities = communityRepository.findAll();
		return communities.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}
	
	public Community get(Integer id) {
		Community community = communityRepository.findById(id).orElse(null);
		if(community == null) {
			LOGGER.error("Community with id: {} doesn't exist!", id);
		}
		return community;
	}
	
	public Community getByName(String name) {
		Community community = communityRepository.findByName(name);
		if(community == null) {
			LOGGER.error("Community with name: {} doesn't exist!", name);
		}
		return community;
	}

	public Community create(CommunityDTO dto, String userName) {
		Community community = convertToEntity(dto);
		community.setCreationDate(LocalDate.now().toString());
		community.setSuspended(false);
		community.setSuspendedReason("");
		User user = userRepository.findByUsername(userName);
		if (user.getUserType().equals("User")) {
			userRepository.updateUserType("Moderator", user.getId());
			userRepository.flush();
		}
		List<Moderator> moderators = new ArrayList<>();
		moderators.add(moderatorRepository.findById(user.getId()).orElse(null));
		community.setModerators(moderators);
		communityRepository.save(community);
		LOGGER.info("Successfully created community: {}", community);
		return community;
	}

	public Community update(CommunityDTO communityDTO, String user) {
			return create(communityDTO, user);
	}
	
	public void delete(Community community) {
		communityRepository.delete(community);
		LOGGER.info("Successfully deleted comment with id: {}", community.getId());
	}
	
	public boolean checkIfCommunityExists(CommunityDTO dto) {
		Community existing = communityRepository.findByName(dto.getName());
		if (existing != null) {
			LOGGER.error("Community with name: {} already exists!", dto.getName());
			return true;
		}
		return false;
	}

	//CONVERSIONS
	
	public CommunityDTO convertToDTO(Community community) {
		CommunityDTO communityDTO = mapper.map(community, CommunityDTO.class);
		
		List<Integer> bannedListIds = new ArrayList<>();
		for (Banned banned : ListUtils.emptyIfNull(community.getBannedList())) {
			bannedListIds.add(banned.getId());
		}
		communityDTO.setBannedListIds(bannedListIds);
		
		List<Integer> rulesIds = new ArrayList<>();
		for (Rule rule : ListUtils.emptyIfNull(community.getRules())) {
			rulesIds.add(rule.getId());
		}
		communityDTO.setRulesIds(rulesIds);
		
		List<Integer> postIds = new ArrayList<>();
		for (Post post : ListUtils.emptyIfNull(community.getPosts())) {
			postIds.add(post.getId());
		}
		communityDTO.setPostsIds(postIds);
		
		List<Integer> moderatorIds = new ArrayList<>();
		for (Moderator moderator : ListUtils.emptyIfNull(community.getModerators())) {
			moderatorIds.add(moderator.getId());
		}
		communityDTO.setModeratorsIds(moderatorIds);
		
		List<Integer> flairIds = new ArrayList<>();
		for (Flair flair : ListUtils.emptyIfNull(community.getFlairs())) {
			flairIds.add(flair.getId());
		}
		communityDTO.setFlairsIds(flairIds);
		
		return communityDTO;
	}
	
	public Community convertToEntity(CommunityDTO communityDTO) {
		Community community = mapper.map(communityDTO, Community.class);
		for (Integer id : ListUtils.emptyIfNull(communityDTO.getBannedListIds())) {
			community.getBannedList().add(bannedRepository.findById(id).orElse(null));
		}
		for (Integer id : ListUtils.emptyIfNull(communityDTO.getRulesIds())) {
			community.getRules().add(ruleRepository.findById(id).orElse(null));
		}
		for (Integer id : ListUtils.emptyIfNull(communityDTO.getPostsIds())) {
			community.getPosts().add(postRepository.findById(id).orElse(null));
		}
		for (Integer id : ListUtils.emptyIfNull(communityDTO.getModeratorsIds())) {
			community.getModerators().add(moderatorRepository.findById(id).orElse(null));
		}
		for (Integer id : ListUtils.emptyIfNull(communityDTO.getFlairsIds())) {
			community.getFlairs().add(flairRepository.findById(id).orElse(null));
		}
		return community;
	}
}
