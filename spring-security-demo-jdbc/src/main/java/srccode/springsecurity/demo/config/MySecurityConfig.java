package srccode.springsecurity.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private DataSource ds;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		// Use JDBC Authentication

		auth.jdbcAuthentication().dataSource(ds);
	}

	// Custom Log In Form
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().antMatchers("/").hasRole("EMP").antMatchers("/admin/**")
		          .hasRole("ADMIN").antMatchers("/managers/**").hasRole("MANAGER").and()
		          .formLogin().loginPage("/showmyLoginPage")
		          .loginProcessingUrl("/authenticateTheUser").permitAll().and().logout()
		          .permitAll().and().exceptionHandling().accessDeniedPage("/access-denied");
	}

}
