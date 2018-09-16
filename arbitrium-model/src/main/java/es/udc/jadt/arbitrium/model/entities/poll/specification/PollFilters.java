package es.udc.jadt.arbitrium.model.entities.poll.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.util.SpecificationFilter;

public class PollFilters {

	public static Specification<Poll> pollKeywordsFilter(final List<String> keywords, final boolean onDescriptionToo) {

		SpecificationFilter<Poll> filter = new SpecificationFilter<Poll>() {

			@Override
			public Predicate toPredicate(Root<Poll> poll, CriteriaQuery<?> query, CriteriaBuilder builder) {

				Predicate predicate = null;
				if (keywords != null) {
					for (String keyword : keywords) {
						Predicate newPred = builder.like(builder.lower(getTitleField(poll)),
								"%" + keyword.toLowerCase() + "%");

						if (onDescriptionToo) {
							newPred = builder.or(newPred, builder.like(builder.lower(getDescriptionField(poll)),
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
		// Testing and debug purpouses
		filter.setArgs(new Object[] { keywords, Boolean.valueOf(onDescriptionToo) });
		return filter;

	}

	private static Path<String> getTitleField(Root<Poll> poll) {
		return poll.<String>get("name");
	}

	private static Path<String> getDescriptionField(Root<Poll> poll) {

		return poll.<String>get("description");
	}
}
