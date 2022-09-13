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

import igor.osa.reddit.be.dto.CommunityDTO;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.service.CommunityService;

@RestController
@RequestMapping(value = "/community")
public class CommunityController {
	
	@Autowired
	private CommunityService communityService;
	
	@GetMapping
	public ResponseEntity<List<CommunityDTO>> getAll(){ 
		return new ResponseEntity<List<CommunityDTO>>(communityService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CommunityDTO> get(@PathVariable("id") Integer id){
		Community community = communityService.get(id);
		if(community == null) {
			return new ResponseEntity<CommunityDTO>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<CommunityDTO>(communityService.convertToDTO(community), HttpStatus.OK);
		}
	}
	
	@PostMapping
	public ResponseEntity<CommunityDTO> create(@RequestBody CommunityDTO communityDTO){
		Community community = communityService.create(communityDTO);
		if (community == null) {
			return new ResponseEntity<CommunityDTO>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<CommunityDTO>(communityService.convertToDTO(community), HttpStatus.OK);
		}
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CommunityDTO> update(@RequestBody CommunityDTO communityDTO, @PathVariable("id") Integer id){
		Community community = communityService.get(id);
		if(community == null) {
			return new ResponseEntity<CommunityDTO>(HttpStatus.BAD_REQUEST);
		}
		community = communityService.update(communityDTO);
		return new ResponseEntity<CommunityDTO>(communityService.convertToDTO(community), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		Community community = communityService.get(id);
		if (community == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			communityService.delete(community);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}
}