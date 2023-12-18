package rkis_8.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import rkis_8.UserService;

/**
 * Класс конфигурации, подключающий SpringSecurity к проекту.
 * Включает в себя разграничение доступа пользователей к страницам, дешифрование хэшированных паролей
 * и добавление начальных пользователей в БД
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    UserService userService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        rkis_8.User defaultUser = new rkis_8.User("user", "password", "USER");
        userService.saveUser(defaultUser);
        rkis_8.User admin = new rkis_8.User("admin", "admin", "ADMIN");
        userService.saveUser(admin);

    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/registration", "/logout", "/**").permitAll()
                        //.requestMatchers("/perfumery").hasAnyAuthority("USER", "ADMIN")
                        //.requestMatchers("/**").hasAuthority("ADMIN")
                        //.requestMatchers("")
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .permitAll()
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

}
