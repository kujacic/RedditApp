package igor.osa.reddit.be.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Flair;

public interface FlairRepository extends JpaRepository<Flair, Integer>{
	Flair findByName(String name);
}
