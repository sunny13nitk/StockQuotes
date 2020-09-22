package root.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import root.user.services.interfaces.IUserService;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter
{
	// add a reference to our security data source
	@Autowired
	private IUserService userService;
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Override
	protected void configure(
	        AuthenticationManagerBuilder auth
	) throws Exception
	{
		
		/*
		 * Use AuthBuilderManger Authentication reinforced by IUserService impl & Password Encoder
		 */
		
		auth.authenticationProvider(authenticationProvider());
		
	}
	
	@Override
	protected void configure(
	        HttpSecurity http
	) throws Exception
	{
		/*
		 * Every New User assigned the DEfault role of USER, USER role can access "/" and "/pf/*", ADMIN role can access
		 * "/scrips/*" and "/users/**", MANAGER role can access "/reports/*"
		 */
		http.authorizeRequests().antMatchers("/css/**").permitAll().antMatchers("/").hasRole("USER")
		        .antMatchers("/pf/**").hasRole("USER").antMatchers("/fl/**").hasRole("USER").antMatchers("/scrips/**")
		        .hasRole("ADMIN").antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/reports/**")
		        .hasRole("MANAGER").and().formLogin().loginPage("/myLoginPage").loginProcessingUrl("/authenticateUser")
		        .successHandler(customAuthenticationSuccessHandler).permitAll().and().logout().permitAll().and()
		        .exceptionHandling().accessDeniedPage("/access-denied");
		
	}
	
	/*
	 * @Bean public BCryptPasswordEncoder passwordEncoder( ) { return new BCryptPasswordEncoder(); }
	 */
	
	// password Encoder Bean - Let Spring decide the Password Encoder
	@Bean
	public static PasswordEncoder passwordEncoder(
	)
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	/*
	 * DAO Authentication Provider bean is needed to set AuthenticationManagerBuilder in configure(auth) method this
	 * auth provider bean needs to have a User Details Service impl of std. Spring Interface UserDetailsService Core
	 * interface which loads user-specific data. It is used throughout the framework as a user DAO (service IMPL) and is
	 * the strategy used by the DaoAuthenticationProvider. The interface requires only one read-only method, which
	 * simplifies support for newdata-access strategies.
	 */
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider(
	)
	{
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService); // set the custom user details service
		auth.setPasswordEncoder(passwordEncoder()); // set the password encoder - bcrypt
		return auth;
	}
}
