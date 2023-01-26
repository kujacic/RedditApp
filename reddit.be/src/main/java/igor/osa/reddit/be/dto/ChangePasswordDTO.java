package igor.osa.reddit.be.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
	private String oldPassword;
	private String newPassword;
}