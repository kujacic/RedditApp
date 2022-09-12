package igor.osa.reddit.be.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "rule")
public class Rule {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id", unique = true, nullable = false)
    private Integer id;
	
	@Column(name = "description", unique = false, nullable = false)
	private String description;
	
	@ManyToOne()
	private Community community;
}