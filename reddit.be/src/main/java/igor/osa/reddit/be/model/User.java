package igor.osa.reddit.be.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "users")
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "userType", discriminatorType = STRING)
@DiscriminatorValue("User")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;
	
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@Column(name = "password", unique = false, nullable = false)
	private String password;
	
	@Column(name = "email", unique = false, nullable = false)
	private String email;
	
	@Column(name = "avatar", unique = false, nullable = true)
	private String avatar;
	
	@Column(name = "registration_date", unique = false, nullable = false)
	private LocalDate registrationDate;
	
	@Column(name = "description", unique = false, nullable = true)
	private String description;
	
	@Column(name = "displayName", unique = false, nullable = true)
	private String displayName;
	
	@Column(name = "userType", insertable = false, updatable = false)
	private String userType;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;
}