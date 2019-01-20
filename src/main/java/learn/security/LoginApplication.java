package learn.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@SpringBootApplication
public class LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

    @Bean
    UserDetailsManager manager(){
        return new InMemoryUserDetailsManager();
    }

    @Bean
    InitializingBean initializingBean(UserDetailsManager manager) {
        return () -> {
            UserDetails josh = User.withDefaultPasswordEncoder().username("jlong").password("password").roles("USER").build();
            manager.createUser(josh);

            UserDetails rob = User.withUserDetails(josh).username("rwinch").build();
            manager.createUser(rob);
        };
    }
}

@Controller
class LoginController {

    @GetMapping("/")
    public String index() {
        return "hidden";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout";
    }
}

@ControllerAdvice
class PrincipalControllerAdvice {
    @ModelAttribute("currentUser")
    Principal principal(Principal principal) {
        return principal;
    }
}

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
            .loginPage("/login")
            .permitAll();
        http.logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/logout-success");
        http.authorizeRequests().anyRequest().authenticated();
    }
}


