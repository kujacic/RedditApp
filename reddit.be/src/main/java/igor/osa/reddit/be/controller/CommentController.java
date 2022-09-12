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
import org.springframework.web.bind.annotation.RestController;

import igor.osa.reddit.be.dto.CommentDTO;
import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.service.CommentService;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService;

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
}