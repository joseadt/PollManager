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

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public Object getEntityId() {
		return entityId;
	}
	

}
