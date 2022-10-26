package igor.osa.reddit.be.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import igor.osa.reddit.be.model.enums.ReactionType;
import lombok.Data;

@Data
@Entity
@Table(name = "reaction")
public class Reaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id", unique = true, nullable = false)
    private Integer id;

	@Enumerated
    @Column(name = "type", nullable = false, columnDefinition = "smallint")
	private ReactionType type;
	
	@Column(name = "timestamp", unique = false, nullable = false)
	private LocalDate timeStamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Comment comment;
}