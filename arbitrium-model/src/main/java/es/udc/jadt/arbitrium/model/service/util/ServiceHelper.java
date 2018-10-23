package es.udc.jadt.arbitrium.model.service.util;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.jadt.arbitrium.model.entities.pollconfig.PollConfiguration;
import es.udc.jadt.arbitrium.model.entities.polltype.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.service.util.exceptions.EntityNotFoundException;

public class ServiceHelper {

	/**
	 * Helper to maintain the logic in getting an object from database in only one
	 * place.
	 *
	 * @param <E>
	 *            Type of the Entity that you want to find
	 * @param <ID>
	 *            Type of the id of the Entity
	 * @param <R>
	 *            Type of the repository
	 * @param repository
	 *            the repository
	 * @param id
	 *            the id
	 * @param entityClass
	 *            the entity class. To launch in a common way the NotFoundException
	 * @return the Entity
	 * @throws EntityNotFoundException
	 *             the entity not found exception
	 */
	public static <E, ID, R extends JpaRepository<E, ID>> E findOneById(R repository, ID id, Class<E> entityClass)
			throws EntityNotFoundException {

		Optional<E> optional = repository.findById(id);

		return optional.orElseThrow(() -> new EntityNotFoundException(entityClass, id));
	}

	/**
	 * Adapt poll configuration based on poll type parameters.
	 *
	 * @param target
	 *            the target
	 * @param parameters
	 *            the parameters
	 */
	public static void adaptPollConfigurationToParameters(PollConfiguration target,
			ConfigurationParameters parameters) {
		if (Boolean.FALSE.equals(parameters.getIsMultiSelection())) {
			final Integer integerOne = Integer.valueOf(1);
			target.setMaxSelectableOptions(integerOne);
			target.setMaxVotesPerUser(integerOne);
		}

		if (parameters.getMaxOptions() != null
				&& parameters.getMaxOptions().compareTo(target.getMaxSelectableOptions()) < 0) {
			target.setMaxSelectableOptions(parameters.getMaxOptions());
		}
		target.checkAttributes();

	}

}
