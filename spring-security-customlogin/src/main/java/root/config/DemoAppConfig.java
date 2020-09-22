package root.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableAspectJAutoProxy(
        proxyTargetClass = true
)
@ComponentScan(
        basePackages = "root"
)
@PropertySource(
    "classpath:persistence-sqlserver.properties"
)
public class DemoAppConfig implements WebMvcConfigurer
{
	@Autowired
	private Environment env; // To access SQL Server Properties from config file in Property Source to set Data Source
	                         // Bean in this APPConfig Class
	
	// define a bean for ViewResolver
	@Bean
	public ViewResolver viewResolver(
	)
	{
		
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		
		return viewResolver;
	}
	
	// define a Bean for DataSource
	@Bean
	public DataSource securityDataSource(
	)
	{
		// create connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
		
		// set JDBC driver
		try
		{
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
		} catch (PropertyVetoException exc)
		{
			
			throw new RuntimeException(exc);
		}
		
		// set the connection props
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		
		// set connection pool props
		
		securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
		
		securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		
		securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
		
		securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));
		
		return securityDataSource;
	}
	
	// Bean for Session Factory
	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(
	)
	{
		
		// create session factory
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		// set the properties
		sessionFactory.setDataSource(securityDataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		
		return sessionFactory;
	}
	
	// Bean for Hibernate Transaction Manager
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(
	        SessionFactory sessionFactory
	        )
	{
		
		// setup transaction manager based on session factory
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		
		return txManager;
	}
	
	/**
	 * ------- Resources Handling ------------
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(
	        ResourceHandlerRegistry registry
	        )
	{
		
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		
		// registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
	}
	
	/*
	 * ----HELPER PRIVATE METHODS ------------------------
	 */
	// need a helper method
	// read environment property and convert to int
	
	private int getIntProperty(
	        String propName
	        )
	{
		
		String propVal = env.getProperty(propName);
		
		// now convert to int
		int intPropVal = Integer.parseInt(propVal);
		
		return intPropVal;
	}
	
	private Properties getHibernateProperties(
	)
	{
		
		// set hibernate properties
		Properties props = new Properties();
		
		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		
		return props;
	}
	
}
