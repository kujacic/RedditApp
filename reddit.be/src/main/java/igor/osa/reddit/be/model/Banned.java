package igor.osa.reddit.be.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "banned")
public class Banned {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banned_id", unique = true, nullable = false)
    private Integer id;
	
	@Column(name = "timestamp", unique = false, nullable = false)
	private LocalDate timestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Moderator by;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Community community;
}