package com.bookretail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPAConfig {
    @Value("${application.db.url}")
    private String url;

    @Value("${application.db.driver}")
    private String driver;

    @Value("${application.db.dialect}")
    private String dialect;

    @Value("${application.db.username}")
    private String username;

    @Value("${application.db.password}")
    private String password;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());

        em.setPackagesToScan("com.bookretail.model");
        em.setPersistenceUnitName("org.hibernate.jpa.HibernatePersistenceProvider");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();

//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
//        properties.setProperty("hibernate.use_sql_comments", "true");

        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.connection.handling_mode", "delayed_acquisition_and_release_after_transaction");
        properties.setProperty("javax.persistence.schema-generation.database.action", "none");

        properties.setProperty(
                "hibernate.physical_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy"
        );
        properties.setProperty(
                "hibernate.implicit_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
        );
        return properties;
    }
}
