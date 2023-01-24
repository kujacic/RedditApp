package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByUsername(String username);
	
	User findByEmail(String email);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET user_type = :usertype where user_id = :id", nativeQuery = true)
	void updateUserType(@Param("usertype") String usertype, @Param("id") Integer id);
}