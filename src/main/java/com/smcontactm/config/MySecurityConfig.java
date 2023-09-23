package com.smcontactm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MySecurityConfig   {
	
	@Bean
	protected UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
 
	@Bean
	protected BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//for jwt purpose added
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationfilter;
	/*
	 * @Bean protected AuthenticationManager authenticationManagerBean() { return
	 * new AuthenticationManagerBuilder() ;
	 * 
	 * }
	 */
	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	 
	
	
	@Bean
	protected DaoAuthenticationProvider getDaoAuthenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.getUserDetailsService());
		authenticationProvider.setPasswordEncoder(this.getBCryptPasswordEncoder());
		
		return authenticationProvider;
	}
	
	
	//configure method
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	     auth.authenticationProvider(getDaoAuthenticationProvider());
	}
	
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
                .authorizeHttpRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/api/getToken/**").permitAll()
                .antMatchers("/api/**")
                .authenticated()
                .antMatchers("/**")//
                .permitAll()//   .anyRequest() //any other request       .authenticated() //do authentication
                .and()

                //.formLogin(withDefaults());
                // .formLogin(login -> login.loginPage("/getLoginPage")).csrf(csrf -> csrf.disable());
                .formLogin(login -> login
                                .loginPage("/getLoginPage")
                                .defaultSuccessUrl("/user/index")
                //.failureUrl("/login-fail")
                )

                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable) ;
               // .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // making session managment policy stateless for jwt and works only id this line is enable
              //  .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        
        //all request before this filter will excute first bcs we added once per request filter before all request it will intercept first time and validate jwt token
        httpSecurity.addFilterBefore(jwtAuthenticationfilter, UsernamePasswordAuthenticationFilter.class);
       
        
		return httpSecurity.build(); 
		
		
	}
}
