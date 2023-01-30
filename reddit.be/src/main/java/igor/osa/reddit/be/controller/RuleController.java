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

import igor.osa.reddit.be.dto.RuleDTO;
import igor.osa.reddit.be.model.Rule;
import igor.osa.reddit.be.service.RuleService;

@RestController
@RequestMapping(value = "/rule")
public class RuleController {
	
	@Autowired
	private RuleService ruleService;
	
	@GetMapping(value = "/community")
	public ResponseEntity<List<RuleDTO>> getByCommunity(@RequestParam(value="communityName") String communityName){
		List<Rule> rules = ruleService.getByCommunity(communityName);
		if(rules == null) {
			return new ResponseEntity<List<RuleDTO>>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<List<RuleDTO>>(ruleService.convertListToDTO(rules), HttpStatus.OK);
		}
	}
	
	@PostMapping
	public ResponseEntity<RuleDTO> create(@RequestBody RuleDTO ruleDTO){
		if (!ruleService.checkIfCommunityExists(ruleDTO.getCommunityName())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			Rule rule = ruleService.create(ruleDTO);
			if (rule != null) {
				return new ResponseEntity<>(ruleService.convertToDTO(rule), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	@PutMapping(value="/update")
	public ResponseEntity<RuleDTO> update(@RequestBody RuleDTO ruleDTO){
		if (!ruleService.checkIfCommunityExists(ruleDTO.getCommunityName())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			Rule rule = ruleService.update(ruleDTO);
			if (rule != null) {
				return new ResponseEntity<>(ruleService.convertToDTO(rule), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
		boolean deleted = ruleService.delete(id);
		if (deleted) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}