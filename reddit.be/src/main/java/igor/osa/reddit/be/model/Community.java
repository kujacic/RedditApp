package igor.osa.reddit.be.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import lombok.Data;

@Data
@Entity
@Table(name = "community")
public class Community {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id", unique = true, nullable = false)
    private Integer id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "description", unique = false, nullable = false)
	private String description;
	
	@Column(name = "creation_date", unique = false, nullable = false)
	private String creationDate;
	
	@Column(name = "suspended", nullable = false)
	private boolean isSuspended;
	
	@Column(name = "suspended_reason", unique = false, nullable = true)
	private String suspendedReason;
	
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Banned> bannedList;
	
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Rule> rules;
	
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Post> posts;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "community_moderator",
			joinColumns = @JoinColumn(name = "community_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<Moderator> moderators;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "community_flair",
    	joinColumns = @JoinColumn(name = "community_id"),
    	inverseJoinColumns = @JoinColumn(name = "flair_id"))
	private List<Flair> flairs;
}