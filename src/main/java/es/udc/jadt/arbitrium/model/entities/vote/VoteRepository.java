package es.udc.jadt.arbitrium.model.entities.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	Vote findOneByIdAndUserEmail(Long id, String email);

}
