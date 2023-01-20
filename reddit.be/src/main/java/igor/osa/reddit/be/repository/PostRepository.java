package igor.osa.reddit.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Community;
import igor.osa.reddit.be.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

	List<Post> findByCommunity(Community community);
}
