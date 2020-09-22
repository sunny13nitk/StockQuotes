package srccode.springsecurity.demo.config;

import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * MAIN DEMO APP Configuration
 *
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "srccode.springsecurity.demo")
@PropertySource("classpath:persistence-mysql.properties")
public class DemoAppConfig
{

	@Autowired
	private Environment	env;

	private Logger		logger	= Logger.getLogger(this.getClass().getName());

	@Bean
	public ViewResolver viewResolver()
	{

		InternalResourceViewResolver vwResolver = new InternalResourceViewResolver();
		vwResolver.setPrefix("/WEB-INF/view/");
		vwResolver.setSuffix(".jsp");
		return vwResolver;

	}

	@Bean
	public DataSource securityDataSource()
	{
		// Create Connection Pool
		ComboPooledDataSource ds = new ComboPooledDataSource();

		// Set the JDBC Driver in Connection Pool
		try
		{
			ds.setDriverClass(env.getProperty("jdbc.driver"));
		}
		catch (PropertyVetoException e)
		{
			throw new RuntimeException(e);
		}

		// Log the Connection Props - Sanity Check
		logger.info(">>> jdbc url = " + env.getProperty("jdbc.url"));

		// Set Connection Props
		ds.setJdbcUrl(env.getProperty("jdbc.url"));
		/*
		 * ds.setUser(env.getProperty("jdbc.user"));
		 * ds.setPassword(env.getProperty("jdbc.password"));
		 */

		// Set connection Pool Props
		ds.setInitialPoolSize(
		          Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
		ds.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
		ds.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
		ds.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));

		return ds;
	}

}
