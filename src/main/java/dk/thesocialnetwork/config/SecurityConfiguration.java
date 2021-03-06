package dk.thesocialnetwork.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and().
        csrf().disable().authorizeRequests().antMatchers("/login", "/login/createuser").permitAll()
                .antMatchers("/","/dashboard**","/chat**, /profile**, /image**").authenticated()
                .and().formLogin().loginPage("/login").usernameParameter("username")
                .passwordParameter("password").defaultSuccessUrl("/")
                .and().logout().logoutSuccessUrl("/login");
        http.exceptionHandling().accessDeniedPage("/login");
    }

    @Autowired
    public void login(AuthenticationManagerBuilder am) throws Exception {
        am.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, active FROM Account where username = ?")
                .authoritiesByUsernameQuery("select username, role from Account where username = ?");
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
