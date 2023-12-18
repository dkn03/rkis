package rkis_8.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
/**Класс конфигурации, создающий бин bCryptPasswordEncoder, отвечающий за хеширование пароля*/
public class BCryptPasswordEncoderBean {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
