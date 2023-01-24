package igor.osa.reddit.be.model;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Moderator")
public class Moderator extends User {

    @ManyToMany(mappedBy = "moderators", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Community> communities;

	public Moderator(){}

	public Moderator(User user, List<Community> communities) {
		super(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getAvatar(), user.getRegistrationDate(), user.getDescription(), user.getDisplayName(), user.getUserType(), user.getComments(), user.getPosts());
		this.communities = communities;
	}
}