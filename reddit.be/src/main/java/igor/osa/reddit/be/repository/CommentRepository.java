package igor.osa.reddit.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Comment;
import igor.osa.reddit.be.model.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	List<Comment> findByPost(Post post);
	
	List<Comment> findByComment(Comment comment);
}