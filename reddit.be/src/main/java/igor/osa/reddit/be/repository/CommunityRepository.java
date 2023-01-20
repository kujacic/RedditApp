package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer>{
	Community findByName(String name);
}
