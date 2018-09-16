package es.udc.jadt.arbitrium.model.entities;

import org.hibernate.Hibernate;

public final class BaseEntity {

	public static void initializeField(Object field) {
		Hibernate.initialize(field);
	}

}
