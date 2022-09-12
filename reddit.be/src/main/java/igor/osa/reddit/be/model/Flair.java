package igor.osa.reddit.be.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "flair")
public class Flair {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flair_id", unique = true, nullable = false)
    private Integer id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "flairs", fetch = FetchType.LAZY)
	private List<Community> communities;
}