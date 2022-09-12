package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
