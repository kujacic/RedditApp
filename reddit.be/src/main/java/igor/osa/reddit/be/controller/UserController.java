package igor.osa.reddit.be.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import igor.osa.reddit.be.dto.ChangePasswordDTO;
import igor.osa.reddit.be.dto.UserDTO;
import igor.osa.reddit.be.model.User;
import igor.osa.reddit.be.security.TokenUtils;
import igor.osa.reddit.be.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAll(){ 
		return new ResponseEntity<List<UserDTO>>(userService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> get(@PathVariable("id") Integer id){
		User user = userService.get(id);
		if(user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<UserDTO>(userService.convertToDTO(user), HttpStatus.OK);
		}
	}
	
	@GetMapping("/username")
	public ResponseEntity<UserDTO> get(@RequestParam("username") String username){
		User user = userService.getByUsername(username);
		if(user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<UserDTO>(userService.convertToDTO(user), HttpStatus.OK);
		}
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO){
		if (userService.checkIfUserExists(userDTO)) {
			return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
		}
		User user = userService.create(userDTO);
		if (user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<UserDTO>(userService.convertToDTO(user), HttpStatus.OK);
		}
	}
	
	@PutMapping
	public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO, @RequestParam("username") String username){
		User user = userService.getByUsername(username);
		if(user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
		}
		user = userService.update(userDTO, user);
		if (user != null) {
			return new ResponseEntity<UserDTO>(userService.convertToDTO(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		User user = userService.get(id);
		if (user == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			userService.delete(user);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}
	
	@PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            		userDTO.getUsername(), userDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails details = userDetailsService.loadUserByUsername(userDTO.getUsername());
            return new ResponseEntity<String>(tokenUtils.generateToken(details), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>("Login failed", HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("/changePassword")
	public ResponseEntity<Void> updatePassword(@RequestBody ChangePasswordDTO dto, @RequestParam("username") String username) {
		boolean result = userService.updatePassword(dto, username);
		if (!result) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
}