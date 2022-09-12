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

import igor.osa.reddit.be.model.enums.ReportReason;
import lombok.Data;

@Data
@Entity
@Table(name = "report")
public class Report {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", unique = true, nullable = false)
    private Integer id;
	
	@Enumerated
    @Column(name = "reason", nullable = false, columnDefinition = "smallint")
	private ReportReason reason;
	
	@Column(name = "timestamp", unique = false, nullable = false)
	private LocalDate timestamp;

	@Column(name = "accepted", unique = false, nullable = false)
	private boolean accepted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User byUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Comment comment;
}