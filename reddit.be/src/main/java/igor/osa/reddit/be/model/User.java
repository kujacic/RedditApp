package igor.osa.reddit.be.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;

import lombok.Data;

@Data
@Entity
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
	
	@Column(name = "avatar", unique = false, nullable = false)
	private String avatar;
	
	@Column(name = "registration_date", unique = false, nullable = false)
	private LocalDate registrationDate;
	
	@Column(name = "description", unique = false, nullable = false)
	private String description;
	
	@Column(name = "displayName", unique = false, nullable = false)
	private String displayName;
	
	@Column(name = "userType", insertable = false, updatable = false)
	private String userType;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;
}