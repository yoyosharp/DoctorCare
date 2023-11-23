package com.fx23121.DoctorCare.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class SecurityConfiguration {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(auth -> auth.loginPage("/login").loginProcessingUrl("/processLogin").permitAll())
//                .logout(auth -> auth.logoutUrl("/logout").logoutSuccessUrl("/?logout").permitAll())
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
