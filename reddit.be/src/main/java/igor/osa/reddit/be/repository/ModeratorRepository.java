package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Moderator;

public interface ModeratorRepository extends JpaRepository<Moderator, Integer>{
	Moderator findByUsername(String username);
}