package igor.osa.reddit.be.dto;

import java.time.LocalDate;
import java.util.List;

import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;
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
    private List<Comment> comments;
    private List<Post> posts;
}