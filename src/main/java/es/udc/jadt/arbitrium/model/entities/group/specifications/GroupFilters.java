package es.udc.jadt.arbitrium.model.entities.group.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import es.udc.jadt.arbitrium.model.entities.group.UserGroup;
import es.udc.jadt.arbitrium.model.util.SpecificationFilter;

public class GroupFilters {

	public static Specification<UserGroup> groupKeywordsFilter(final List<String> keywords) {

		return new SpecificationFilter<UserGroup>(new Object[] { keywords }) {

			private static final long serialVersionUID = -5433912963788384121L;

			@Override
			public Predicate toPredicate(Root<UserGroup> group, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.isFalse(getIsPrivateField(group));

				Predicate keywordsPredicate = null;

				if (keywords != null && !keywords.isEmpty()) {
					for (String keyword : keywords) {
						Predicate newPred = builder.like(builder.lower(getNameField(group)),
								"%" + keyword.toLowerCase() + "%");

						if (keywordsPredicate != null) {
							keywordsPredicate = builder.or(keywordsPredicate, newPred);
						} else {
							keywordsPredicate = newPred;
						}
					}
				}
				query.orderBy(builder.desc(group.get("id")));
				return (keywordsPredicate == null) ? predicate : builder.and(predicate, keywordsPredicate);
			}
		};
	}

	private static Path<String> getNameField(Root<UserGroup> group) {
		return group.<String>get("name");
	}

	private static Path<Boolean> getIsPrivateField(Root<UserGroup> group) {
		return group.<Boolean>get("isPrivate");
	}
}
