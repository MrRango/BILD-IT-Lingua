package org.bildit.lingua.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/logout").permitAll()
		.antMatchers("/jdbc").permitAll()
		.antMatchers("/get-users").permitAll()
		.antMatchers("/user-account").permitAll()
		.antMatchers("/registration").permitAll()
		.antMatchers("/registration-check").permitAll()
		.anyRequest().denyAll()
		.and()
		.formLogin().and().logout();
	}
	
}
