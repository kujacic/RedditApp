package igor.osa.reddit.be.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.FlairDTO;
import igor.osa.reddit.be.model.Flair;
import igor.osa.reddit.be.repository.FlairRepository;

@Service
public class FlairService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private FlairRepository flairRepository;
	
	public List<Flair> getAll() {
		return flairRepository.findAll();
	}
	
	public Flair getByName(String name) {
		return flairRepository.findByName(name);
	}
	
	public List<String> getFlairNames(List<Flair> flairs) {
		List<String> names = new ArrayList<>();
		for (Flair flair : flairs) {
			names.add(flair.getName());
		}
		return names;
	}
	
	public FlairDTO convertToDTO(Flair flair) {
		return mapper.map(flair, FlairDTO.class);
	}
	
	public Flair convertToEntity(FlairDTO flairDTO) {
		return mapper.map(flairDTO, Flair.class);
	}
	
	public List<FlairDTO> convertListToDTO(List<Flair> flairs) {
		List<FlairDTO> dtos = new ArrayList<>();
		for (Flair flair : flairs) {
			dtos.add(convertToDTO(flair));
		}
		return dtos;
	}
}