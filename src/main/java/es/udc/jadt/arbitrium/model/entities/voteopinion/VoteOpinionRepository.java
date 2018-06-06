package es.udc.jadt.arbitrium.model.voteopinion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.jadt.arbitrium.model.vote.Vote;

@Repository
public interface VoteOpinionRepository extends JpaRepository<VoteOpinion, VoteOpinionPK> {

	List<VoteOpinion> findByVote(Vote vote);
}
