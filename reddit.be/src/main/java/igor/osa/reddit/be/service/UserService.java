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

import igor.osa.reddit.be.dto.ChangePasswordDTO;
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
	
	public User getByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			LOGGER.error("User with username: {} doesn't exist!", username);
		}
		return user;
	}
	
	public boolean checkIfUserExists(UserDTO dto) {
		User existingUsername = userRepository.findByUsername(dto.getUsername());
		if (existingUsername != null) {
			LOGGER.error("User with username: {} already exists!", dto.getUsername());
			return true;
		}
		User existingEmail = userRepository.findByEmail(dto.getEmail());
		if (existingEmail != null) {
			LOGGER.error("User with email: {} already exists!", dto.getEmail());
			return true;
		}
		return false;
	}
	
	public User create(UserDTO dto) {
		User user = convertToEntity(dto);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setUserType("User");
		if(user.getRegistrationDate() == null) {
			user.setRegistrationDate(LocalDate.now());
		}
		userRepository.save(user);
		LOGGER.info("Successfully created user: {}", user);
		return user;
	}
	
	public User update(UserDTO userDTO, User user) {
		if(!userDTO.getUsername().equals(user.getUsername())) {
			if (userRepository.findByUsername(userDTO.getUsername()) != null) {
				return null;
			} else {
				user.setUsername(userDTO.getUsername());
			}
		}
		user.setDisplayName(userDTO.getDisplayName());
		user.setEmail(userDTO.getEmail());
		userRepository.save(user);
		LOGGER.info("Successfully updated user: {}", user);
		return user;
	}
	
	public void delete(User user) {
		userRepository.delete(user);
		LOGGER.info("Successfully deleted user with id: {}", user.getId());
	}
	
	public boolean updatePassword(ChangePasswordDTO dto, String username) {
		User user = userRepository.findByUsername(username);
		if(passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
			userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}
	
	//CONVERSIONS
	
	public UserDTO convertToDTO(User user) {
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		if (user.getUserType().equals("Moderator")) {
			userDTO.setIsModerator(true);
		} else {
			userDTO.setIsModerator(false);
		}
		return userDTO;
	}
	
	public User convertToEntity(UserDTO userDTO) {
		User user = mapper.map(userDTO, User.class);
		return user;
	}
}