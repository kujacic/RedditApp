package igor.osa.reddit.be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.PostDTO;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.repository.CommunityRepository;
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
		post.setCreationDate(LocalDate.now());
		post.setUser(userRepository.findByUsername(dto.getAuthor()));
		post.setCommunity(communityRepository.findByName(dto.getCommunityName()));
		postRepository.save(post);
		LOGGER.info("Successfully created post: {}", post);
		return post;
	}
	
	public Post update(PostDTO postDTO) {
			return create(postDTO);
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
	
	//CONVERSIONS
	
	public PostDTO convertToDTO(Post post) {
		PostDTO postDTO = mapper.map(post, PostDTO.class);
		postDTO.setAuthor(post.getUser().getUsername());
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