package es.udc.jadt.arbitrium.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import es.udc.jadt.arbitrium.BaseApplication;
import es.udc.jadt.arbitrium.model.entities.BaseEntity;

@Configuration
@Profile("test")
@EnableJpaRepositories(basePackageClasses = BaseEntity.class)
@ComponentScan(basePackageClasses = BaseEntity.class)
public class TestConfig {

	@Bean
	@Primary
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("org.hsqldb.jdbcDriver");
		config.setJdbcUrl("jdbc:hsqldb:mem:arbitrium");
		config.setUsername("sa");
		config.setPassword("");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");

		return new HikariDataSource(config);
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);

		String entities = ClassUtils.getPackageName(BaseApplication.class);
		String converters = ClassUtils.getPackageName(Jsr310JpaConverters.class);
		entityManagerFactoryBean.setPackagesToScan(entities, converters);

		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		
		Properties jpaProperties = new Properties();
		jpaProperties.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
		jpaProperties.put(Environment.HBM2DDL_AUTO, "create");
		jpaProperties.put(Environment.SHOW_SQL, "false");
		jpaProperties.put(Environment.FORMAT_SQL, "true");
		jpaProperties.put(Environment.USE_SQL_COMMENTS, "true");
		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}

	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	@Primary
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
