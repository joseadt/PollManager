package es.udc.jadt.arbitrium.model.entities.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	public UserProfile findOneByEmail(String email);
}
