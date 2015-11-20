package springboot.pac4j.conf

import org.pac4j.core.client.Clients
import org.pac4j.springframework.security.authentication.ClientAuthenticationProvider
import org.pac4j.springframework.security.web.ClientAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ApplicationContext context

    @Autowired
    Clients clients

    @Autowired
    ClientAuthenticationProvider clientProvider

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                    "/**/*.css",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.jpg",
                    "/**/*.ico",
                    "/**/*.js"
                )
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()

        http.addFilterBefore(clientFilter(), UsernamePasswordAuthenticationFilter)
    }

    ClientAuthenticationFilter clientFilter() {
        return new ClientAuthenticationFilter(
                clients: clients,
                sessionAuthenticationStrategy: sas(),
                authenticationManager: clientProvider as AuthenticationManager
        )
    }

    @Bean
    SessionAuthenticationStrategy sas() {
        return new SessionFixationProtectionStrategy()
    }
    
}
