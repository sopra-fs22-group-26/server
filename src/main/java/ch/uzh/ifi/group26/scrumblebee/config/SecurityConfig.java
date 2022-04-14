package ch.uzh.ifi.group26.scrumblebee.config;

import ch.uzh.ifi.group26.scrumblebee.security.Entrypoints.AuthEntryPointJwt;
import ch.uzh.ifi.group26.scrumblebee.security.filters.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    private final boolean dev = true;

//    @Bean
//    public AuthTokenFilter authenticationTokenFilter() {
//        return new AuthTokenFilter();
//    }

    @Autowired
    AuthTokenFilter authTokenFilter;

    // configure authentication manager with correct provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // configure web security for URL protection
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // all endpoints need to be authenticated, except the ones specified in .antMatchers("/....")
        if (dev) {
            http.cors().and().csrf().disable()
                    .authorizeRequests().antMatchers("/**").permitAll()
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        } else {
            http.cors().and().csrf().disable()
                    .authorizeRequests().antMatchers("/register**").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

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