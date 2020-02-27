package ca.uqtr.fitbit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        //securedEnabled = true,
        //jsr250Enabled = true,
        proxyTargetClass = true
        )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        /*http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/anonymous*").anonymous()
                .antMatchers("/login*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/homepage.html", true)
                //.failureUrl("/login.html?error=true")
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler());*/

        /*http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();*/
        http .csrf().disable() .authorizeRequests() .anyRequest().permitAll();
        /*http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/index.*", "/", "/registration", "/login").permitAll()
                .anyRequest().authenticated()
                .and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .logout();*/


        /*http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().requestMatchers().antMatchers("/eureka/**")
                .and().authorizeRequests().antMatchers("/eureka/**")
                .hasRole("SYSTEM").anyRequest().denyAll().and()
                .httpBasic().and().csrf().disable();*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
