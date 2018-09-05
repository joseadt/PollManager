package es.udc.jadt.arbitrium.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import es.udc.jadt.arbitrium.BaseApplication;
import es.udc.jadt.arbitrium.model.entities.BaseEntity;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:persistence.properties")
@EnableJpaRepositories(basePackageClasses = BaseEntity.class)
@EnableAutoConfiguration
class JPAConfiguration {

	@Value("${dataSource.driverClassName}")
	private String driver;
	@Value("${dataSource.url}")
	private String url;
	@Value("${dataSource.username}")
	private String username;
	@Value("${dataSource.password}")
	private String password;
	@Value("${hibernate.dialect}")
	private String dialect;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;
	@Value("${hibernate.show_sql}")
	private String showSql;
	@Value("${hibernate.format_sql}")
	private String formatSql;
	@Value("${hibernate.use_sql_comments}")
	private String useSqlComments;

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(this.driver);
		config.setJdbcUrl(this.url);
		config.setUsername(this.username);
		config.setPassword(this.password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");

		return new HikariDataSource(config);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		String entities = ClassUtils.getPackageName(BaseApplication.class);
		String converters = ClassUtils.getPackageName(Jsr310JpaConverters.class);
		entityManagerFactoryBean.setPackagesToScan(entities, converters);

		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", this.dialect);
		jpaProperties.put("hibernate.hbm2ddl.auto", this.hbm2ddlAuto);
		jpaProperties.put("hibernate.show_sql", this.showSql);
		jpaProperties.put("hibernate.format_sql", this.formatSql);
		jpaProperties.put("hibernate.use_sql_comments", this.useSqlComments);
		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}

}
