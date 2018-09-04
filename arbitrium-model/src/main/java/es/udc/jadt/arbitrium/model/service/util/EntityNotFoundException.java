package es.udc.jadt.arbitrium.model.service.util;

public class EntityNotFoundException extends Exception {

	private final Class<?> entityClass;

	private final Object entityId;

	public static final String DEFAULT_MESSAGE_FORMAT = "There is no entity with id %s of the class %s";

	public EntityNotFoundException(Class<?> entityClass, Object entityId) {
		super(String.format(DEFAULT_MESSAGE_FORMAT, entityId.toString(), entityClass.getName()));
		this.entityClass = entityClass;
		this.entityId = entityId;
	}

	public EntityNotFoundException(String msg, Class<?> entityClass, Object entityId) {
		super(msg);
		this.entityClass = entityClass;
		this.entityId = entityId;
	}

	public Class<?> getEntityClass() {
		return this.entityClass;
	}

	public Object getEntityId() {
		return this.entityId;
	}

	/**
	 * Gets the message that you should expect with two given arguments
	 *
	 * @param entityClass
	 *            the entity class
	 * @param entityId
	 *            the entity id
	 * @return the string
	 */
	public static String messageExample(Class<?> entityClass, Object entityId) {
		return String.format(DEFAULT_MESSAGE_FORMAT, entityId.toString(), entityClass.getName());
	}

}
