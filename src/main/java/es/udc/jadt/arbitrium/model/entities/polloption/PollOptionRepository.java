package es.udc.jadt.arbitrium.model.polloption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, PollOptionPk> {

}
