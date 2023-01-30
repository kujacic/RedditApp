package igor.osa.reddit.be.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import igor.osa.reddit.be.dto.CommentDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.service.CommentService;
import igor.osa.reddit.be.service.PostService;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostService postService;

	@GetMapping
	public ResponseEntity<List<CommentDTO>> getAll(){ 
		return new ResponseEntity<List<CommentDTO>>(commentService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CommentDTO> get(@PathVariable("id") Integer id){
		Comment comment = commentService.get(id);
		if(comment == null) {
			return new ResponseEntity<CommentDTO>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<CommentDTO>(commentService.convertToDTO(comment), HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/post")
	public ResponseEntity<List<CommentDTO>> getAllByPost(@RequestParam("postId") String postId){
		Post post = postService.get(Integer.valueOf(postId));
		List<CommentDTO> dtos = commentService.getAllByPost(post);
		if(dtos == null) {
			return new ResponseEntity<List<CommentDTO>>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<List<CommentDTO>>(dtos, HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/post/sorted")
	public ResponseEntity<List<CommentDTO>> getByPostSorted(@RequestParam(value="postId") String postId, @RequestParam(value="sortBy") String sortBy) {
		Post post = postService.get(Integer.valueOf(postId));
		List<CommentDTO> comments = commentService.convertListToDTO(post.getComments());
		if(comments == null) {
			return new ResponseEntity<List<CommentDTO>>(HttpStatus.NOT_FOUND);
		}else {
			List<CommentDTO> sortedComments = commentService.sortComments(comments, sortBy);
			return new ResponseEntity<List<CommentDTO>>(sortedComments, HttpStatus.OK);
		}
	}
	
	@PostMapping
	public ResponseEntity<CommentDTO> create(@RequestBody CommentDTO commentDTO){
		Comment comment = commentService.create(commentDTO);
		if (comment == null) {
			return new ResponseEntity<CommentDTO>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<CommentDTO>(commentService.convertToDTO(comment), HttpStatus.OK);
		}
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CommentDTO> update(@RequestBody CommentDTO commentDTO, @PathVariable("id") Integer id){
		Comment comment = commentService.get(id);
		if(comment == null) {
			return new ResponseEntity<CommentDTO>(HttpStatus.BAD_REQUEST);
		}
		comment = commentService.update(commentDTO);
		return new ResponseEntity<CommentDTO>(commentService.convertToDTO(comment), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		Comment comment = commentService.get(id);
		if (comment == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			commentService.delete(comment);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/parent/{id}")
	public ResponseEntity<CommentDTO> getByParent(@PathVariable("id") Integer id){
		Comment comment = commentService.getByParent(id);
		if(comment == null) {
			return new ResponseEntity<CommentDTO>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<CommentDTO>(commentService.convertToDTO(comment), HttpStatus.OK);
		}
	}
}