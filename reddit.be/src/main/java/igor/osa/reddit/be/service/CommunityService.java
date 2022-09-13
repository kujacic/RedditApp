package igor.osa.reddit.be.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.CommunityDTO;
import igor.osa.reddit.be.model.Banned;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Flair;
import igor.osa.reddit.be.model.Moderator;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Rule;
import igor.osa.reddit.be.repository.BannedRepository;
import igor.osa.reddit.be.repository.CommunityRepository;
import igor.osa.reddit.be.repository.FlairRepository;
import igor.osa.reddit.be.repository.ModeratorRepository;
import igor.osa.reddit.be.repository.PostRepository;
import igor.osa.reddit.be.repository.RuleRepository;

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
	
	public Community create(CommunityDTO dto) {
		Community community = convertToEntity(dto);
		communityRepository.save(community);
		LOGGER.info("Successfully created community: {}", community);
		return community;
	}
	
	public Community update(CommunityDTO communityDTO) {
			return create(communityDTO);
	}
	
	public void delete(Community community) {
		communityRepository.delete(community);
		LOGGER.info("Successfully deleted comment with id: {}", community.getId());
	}

	//CONVERSIONS
	
	public CommunityDTO convertToDTO(Community community) {
		CommunityDTO communityDTO = mapper.map(community, CommunityDTO.class);
		for (Banned banned : community.getBannedList()) {
			communityDTO.getBannedListIds().add(banned.getId());
		}
		for (Rule rule : community.getRules()) {
			communityDTO.getRulesIds().add(rule.getId());
		}
		for (Post post : community.getPosts()) {
			communityDTO.getPostsIds().add(post.getId());
		}
		for (Moderator moderator : community.getModerators()) {
			communityDTO.getModeratorsIds().add(moderator.getId());
		}
		for (Flair flair : community.getFlairs()) {
			communityDTO.getFlairsIds().add(flair.getId());
		}
		return communityDTO;
	}
	
	public Community convertToEntity(CommunityDTO communityDTO) {
		Community community = mapper.map(communityDTO, Community.class);
		for (Integer id : communityDTO.getBannedListIds()) {
			community.getBannedList().add(bannedRepository.findById(id).orElse(null));
		}
		for (Integer id : communityDTO.getRulesIds()) {
			community.getRules().add(ruleRepository.findById(id).orElse(null));
		}
		for (Integer id : communityDTO.getPostsIds()) {
			community.getPosts().add(postRepository.findById(id).orElse(null));
		}
		for (Integer id : communityDTO.getModeratorsIds()) {
			community.getModerators().add(moderatorRepository.findById(id).orElse(null));
		}
		for (Integer id : communityDTO.getFlairsIds()) {
			community.getFlairs().add(flairRepository.findById(id).orElse(null));
		}
		return community;
	}
}
