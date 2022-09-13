package igor.osa.reddit.be.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.PostDTO;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.repository.PostRepository;

@Service
public class PostService {

	private static final Logger LOGGER = LogManager.getLogger(Post.class);
	
	@Autowired
	private PostRepository postRepository;
	
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
	
	//CONVERSIONS
	
	public PostDTO convertToDTO(Post post) {
		PostDTO postDTO = mapper.map(post, PostDTO.class);
		return postDTO;
	}
	
	public Post convertToEntity(PostDTO postDTO) {
		Post post = mapper.map(postDTO, Post.class);
		return post;
	}
}