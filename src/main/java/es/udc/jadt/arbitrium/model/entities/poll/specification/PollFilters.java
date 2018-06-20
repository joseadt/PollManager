package es.udc.jadt.arbitrium.model.entities.poll.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;

public class PollFilters {

	public static Specification<Poll> pollKeywordsFilter(final List<String> keywords, final boolean onDescriptionToo) {

		return new Specification<Poll>() {
			
			@Override
			public Predicate toPredicate(Root<Poll> poll, CriteriaQuery<?> query, CriteriaBuilder builder) {

				Predicate predicate = null;
				if(keywords !=null) {
					for (String keyword : keywords) {
						Predicate newPred = builder.like(builder.lower(getTitleField(poll)),
								"%" + keyword.toLowerCase() + "%");
						
						if (onDescriptionToo) {
							newPred = builder.or(newPred,
									builder.like(builder.lower(getDescriptionField(poll)),
											"%" + keyword.toLowerCase() + "%"));
						}
						
						if (predicate != null) {
							predicate = builder.or(predicate, newPred);
						} else {
							predicate = newPred;
						}

					}
					
				}
				return predicate;
			}
		};

	}

	private static Path<String> getTitleField(Root<Poll> poll) {
		return poll.<String>get("name");
	}

	private static Path<String> getDescriptionField(Root<Poll> poll) {

		return poll.<String>get("description");
	}
}
