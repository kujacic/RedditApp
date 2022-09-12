package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Banned;

public interface BannedRepository extends JpaRepository<Banned, Integer>{

}
