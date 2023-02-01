package igor.osa.reddit.be.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.PostDTO;
import igor.osa.reddit.be.dto.ReactionDTO;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Reaction;
import igor.osa.reddit.be.model.enums.ReactionType;
import igor.osa.reddit.be.repository.CommunityRepository;
import igor.osa.reddit.be.repository.FlairRepository;
import igor.osa.reddit.be.repository.PostRepository;
import igor.osa.reddit.be.repository.UserRepository;

@Service
public class PostService {

	private static final Logger LOGGER = LogManager.getLogger(Post.class);
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private FlairRepository flairRepository;
	
	@Autowired
	private ReactionService reactionService;
	
	@Autowired
	private ModelMapper mapper;
	
	public List<PostDTO> getAll() {
		List<Post> posts = postRepository.findAll();
		return posts.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}
	
	public Post get(Integer id) {
		Post post = postRepository.findById(id).orElse(null);
		if(post == null) {
			LOGGER.error("Post with id: {} doesn't exist!", id);
		}
		return post;
	}
	
	public Post create(PostDTO dto) {
		Post post = convertToEntity(dto);
		post.setCreationDate(LocalDateTime.now());
		post.setUser(userRepository.findByUsername(dto.getAuthor()));
		post.setCommunity(communityRepository.findByName(dto.getCommunityName()));
		post.setFlair(flairRepository.findByName(dto.getFlair()));
		Post saved = postRepository.save(post);
		List<Reaction> reactions = new ArrayList<>();
		Reaction autoUpvote = reactionService.create(new ReactionDTO(null, ReactionType.UPVOTE, post.getUser().getUsername(), saved.getId(), null, LocalDate.now()));
		reactions.add(autoUpvote);
		post.setReactions(reactions);
		postRepository.save(saved);
		LOGGER.info("Successfully created post: {}", post);
		return post;
	}

	public Post update(PostDTO postDTO, Post post) {
		post.setTitle(postDTO.getTitle());
		post.setText(postDTO.getText());
		post.setFlair(flairRepository.findByName(postDTO.getFlair()));
		postRepository.save(post);
		LOGGER.info("Successfully created post: {}", post);
		return post;
	}
	
	public void delete(Post post) {
		postRepository.delete(post);
		LOGGER.info("Successfully deleted post with id: {}", post.getId());
	}
	
	public List<PostDTO> getAllByCommunity(Community community) {
		List<Post> posts = postRepository.findByCommunity(community);
		return posts.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}
	
	public List<PostDTO> sortPosts(List<PostDTO> posts, String sortBy) {
		if (sortBy.equals("newest")) {
			posts.sort(Comparator.comparing(PostDTO::getCreationDate).reversed());
		}
		else if (sortBy.equals("oldest")) {
			posts.sort(Comparator.comparing(PostDTO::getCreationDate));
		}
		else if (sortBy.equals("topRated")) {
			posts.sort(Comparator.comparing(PostDTO::getKarma).reversed());
		}
		else if (sortBy.equals("lowestRated")) {
			posts.sort(Comparator.comparing(PostDTO::getKarma));
		}
		else if (sortBy.equals("hot")) {
			long now = System.currentTimeMillis();
			for (PostDTO dto : posts) {
				Long timeDiff = now - dto.getCreationDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				Long hot = dto.getKarma().longValue() / timeDiff;
				dto.setHotFactor(hot.intValue());
			}
			posts.sort(Comparator.comparing(PostDTO::getHotFactor).reversed());
		}
		return posts;
	}
	
	//CONVERSIONS
	
	public PostDTO convertToDTO(Post post) {
		PostDTO postDTO = mapper.map(post, PostDTO.class);
		postDTO.setCommunityName(post.getCommunity().getName());
		postDTO.setAuthor(post.getUser().getUsername());
		if (post.getUser().getDisplayName() != null) {
			postDTO.setAuthorDisplayName(post.getUser().getDisplayName());
		}
		if (post.getFlair() != null) {
			postDTO.setFlair(post.getFlair().getName());
		}
		
		List<Reaction> allReactions = post.getReactions();
        if (allReactions != null) {
        	List<Reaction> upvotes = new ArrayList<>();
            List<Reaction> downvotes = new ArrayList<>();
        	for (Reaction reaction : allReactions) {
                if (reaction.getType().equals(ReactionType.UPVOTE)) {
                    upvotes.add(reaction);
                } else if (reaction.getType().equals(ReactionType.DOWNVOTE)) {
                    downvotes.add(reaction);
                }
            }
    		postDTO.setKarma(upvotes.size() - downvotes.size());
        }
		return postDTO;
	}
	
	public Post convertToEntity(PostDTO postDTO) {
		Post post = mapper.map(postDTO, Post.class);
		return post;
	}
	
	public List<PostDTO> convertListToDTO(List<Post> posts) {
		List<PostDTO> dtos = new ArrayList<>();
		for (Post post : posts) {
			dtos.add(convertToDTO(post));
		}
		return dtos;
	}
}