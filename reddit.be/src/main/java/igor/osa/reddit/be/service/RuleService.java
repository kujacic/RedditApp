package igor.osa.reddit.be.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.RuleDTO;
import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Rule;
import igor.osa.reddit.be.repository.RuleRepository;

@Service
public class RuleService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private RuleRepository ruleRepository;
	
	public Rule create(RuleDTO ruleDTO) {
		Rule rule = convertToEntity(ruleDTO);
		ruleRepository.save(rule);
		return rule;
	}
	
	public Rule update(RuleDTO ruleDTO) {
		Rule rule = ruleRepository.findById(ruleDTO.getId()).orElse(null);
		rule.setDescription(ruleDTO.getDescription());
		ruleRepository.save(rule);
		return rule;
	}
	
	public boolean delete(Integer id) {
		Rule rule = ruleRepository.findById(id).orElse(null);
		if (rule != null) {
			ruleRepository.delete(rule);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Rule> getByCommunity(String communityName) {
		Community community = communityService.getByName(communityName);
		return community.getRules();
	}
	
	public boolean checkIfCommunityExists(String communityName) {
		Community community = communityService.getByName(communityName);
		if (community != null) {
			return true;
		} else {
			return false;
		}
	}

	//CONVERSIONS
	
	public RuleDTO convertToDTO(Rule rule) {
		RuleDTO ruleDTO = mapper.map(rule, RuleDTO.class);
		ruleDTO.setCommunityName(rule.getCommunity().getName());
		return ruleDTO;
	}
	
	public Rule convertToEntity(RuleDTO ruleDTO) {
		Rule rule = mapper.map(ruleDTO, Rule.class);
		rule.setCommunity(communityService.getByName(ruleDTO.getCommunityName()));
		return rule;
	}
	
	public List<RuleDTO> convertListToDTO(List<Rule> rules) {
		List<RuleDTO> dtos = new ArrayList<>();
		for (Rule rule : rules) {
			dtos.add(convertToDTO(rule));
		}
		return dtos;
	}
}