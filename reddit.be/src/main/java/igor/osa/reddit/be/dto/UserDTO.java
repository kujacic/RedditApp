package igor.osa.reddit.be.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserDTO {
	
	private Integer id;
	private String username;
	private String password;
	private String email;
	private String avatar;
	private LocalDate registrationDate;
	private String description;
	private String displayName;
	private String userType;
}