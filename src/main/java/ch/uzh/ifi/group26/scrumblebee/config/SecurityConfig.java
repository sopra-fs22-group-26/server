package ch.uzh.ifi.group26.scrumblebee.config;

import ch.uzh.ifi.group26.scrumblebee.security.entrypoints.AuthEntryPointJwt;
import ch.uzh.ifi.group26.scrumblebee.security.filters.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("securityUserDetailsService")
    UserDetailsService securityUserDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    AuthTokenFilter authTokenFilter;

    // configure authentication manager with correct provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService);
    }

    // configure web security for URL protection
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // all endpoints need to be authenticated, except the ones specified in .antMatchers("/....")
        // Use the following code if endpoint should not be secured
        /*
        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         */

        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/register**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(this.authTokenFilter, BasicAuthenticationFilter.class);
    }

    /**
     *
     * @return the authenticationManager bean from the super class WebSecurityConfigurerAdapter
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
