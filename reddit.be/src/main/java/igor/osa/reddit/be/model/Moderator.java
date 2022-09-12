package igor.osa.reddit.be.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Moderator")
public class Moderator extends User {
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Community community;
}