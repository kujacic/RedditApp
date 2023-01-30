package igor.osa.reddit.be.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", unique = true, nullable = false)
    private Integer id;

	@Column(name = "text", unique = false, nullable = false)
	private String text;
	
	@Column(name = "timestamp", unique = false, nullable = false)
	private LocalDate timestamp;
	
	@Column(name = "deleted", nullable = false)
	private boolean isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = true)
	private Comment comment;
	
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Report> reports;
	
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Reaction> reactions;
}