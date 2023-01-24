package igor.osa.reddit.be.repository;

import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;
import igor.osa.reddit.be.model.Reaction;
import igor.osa.reddit.be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {

    List<Reaction> getAllByUser(User user);

    List<Reaction> getAllByPost(Post post);

    Reaction findByUserAndPost(User user, Post post);
}