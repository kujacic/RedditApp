package igor.osa.reddit.be.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import igor.osa.reddit.be.dto.CommunityDTO;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Flair;
import igor.osa.reddit.be.service.CommunityService;
import igor.osa.reddit.be.service.FlairService;

@RestController
@RequestMapping(value = "/community")
public class CommunityController {
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private FlairService flairService;
	
	@GetMapping
	public ResponseEntity<List<CommunityDTO>> getAll(){ 
		return new ResponseEntity<List<CommunityDTO>>(communityService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/moderator")
	public ResponseEntity<List<CommunityDTO>> getAllForModerator(@RequestParam("user") String user) { 
		List<Community> moderatorCommunities = communityService.getByModerator(user);
		return new ResponseEntity<List<CommunityDTO>>(communityService.convertListToDTO(moderatorCommunities), HttpStatus.OK);
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
	public ResponseEntity<Void> create(@RequestBody CommunityDTO communityDTO, @RequestParam String user ){
		if (communityService.checkIfCommunityExists(communityDTO)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Community community = communityService.create(communityDTO, user);
		if (community == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CommunityDTO> update(@RequestBody CommunityDTO communityDTO, @PathVariable("id") Integer id, @RequestParam String user){
		Community community = communityService.get(id);
		if(community == null) {
			return new ResponseEntity<CommunityDTO>(HttpStatus.BAD_REQUEST);
		}
		community = communityService.update(communityDTO, user);
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
	
	@GetMapping(value = "/flair")
	public ResponseEntity<List<String>> getFlairsForCommunity(@RequestParam("community") String communityName){
		Community community = communityService.getByName(communityName);
		if(community == null) {
			return new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<List<String>>(flairService.getFlairNames(community.getFlairs()), HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/flair/missing")
	public ResponseEntity<List<String>> getFlairsMissingForCommunity(@RequestParam("community") String communityName){
		Community community = communityService.getByName(communityName);
		if(community == null) {
			return new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND);
		}else {
			List<Flair> allFlairs = flairService.getAll();
			for (Flair flair : community.getFlairs()) {
				if (allFlairs.contains(flair)) {
					allFlairs.remove(flair);
				}
			}
			return new ResponseEntity<List<String>>(flairService.getFlairNames(allFlairs), HttpStatus.OK);
		}
	}
	
	@PostMapping(value = "/flair/add")
	public ResponseEntity<Void> addFLair(@RequestParam("community") String communityName, @RequestParam("flair") String flairName){
		communityService.addFlair(communityName, flairName);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/flair/remove")
	public ResponseEntity<Void> removeFLair(@RequestParam("community") String communityName, @RequestParam("flair") String flairName){
		communityService.removeFlair(communityName, flairName);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}