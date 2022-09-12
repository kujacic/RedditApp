package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

}
