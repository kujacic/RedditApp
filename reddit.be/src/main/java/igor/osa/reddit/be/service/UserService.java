package igor.osa.reddit.be.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import igor.osa.reddit.be.dto.UserDTO;
import igor.osa.reddit.be.model.User;
import igor.osa.reddit.be.repository.UserRepository;

@Service
public class UserService {
	
	private static final Logger LOGGER = LogManager.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper mapper;
	
	public List<UserDTO> getAll() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}
	
	public User get(Integer id) {
		User user = userRepository.findById(id).orElse(null);
		if(user == null) {
			LOGGER.error("User with id: {} doesn't exist!", id);
		}
		return user;
	}
	
	public User create(UserDTO dto) {
		User user = convertToEntity(dto);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		if(user.getRegistrationDate() == null) {
			user.setRegistrationDate(LocalDate.now());
		}
		userRepository.save(user);
		LOGGER.info("Successfully created user: {}", user);
		return user;
	}
	
	public User update(UserDTO userDTO) {
			return create(userDTO);
	}
	
	public void delete(User user) {
		userRepository.delete(user);
		LOGGER.info("Successfully deleted user with id: {}", user.getId());
	}
	
	//CONVERSIONS
	
	public UserDTO convertToDTO(User user) {
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		return userDTO;
	}
	
	public User convertToEntity(UserDTO userDTO) {
		User user = mapper.map(userDTO, User.class);
		return user;
	}
}