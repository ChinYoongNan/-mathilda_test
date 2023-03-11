package com.caam.mrs.api.config;

import com.caam.mrs.api.security.CustomUserDetailsService;
import com.caam.mrs.api.security.JwtAuthenticationEntryPoint;
import com.caam.mrs.api.security.JwtAuthenticationFilter;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * Created by Suhaimi AK on 27/03/2019.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@ComponentScan({ "com.caam.mrs.api.security" })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Arrays.asList(domainUrl));
        // config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedOriginPatterns(Collections.singletonList("*"));      
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");    
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
//                .ignoringAntMatchers("/api/auth/signin")
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                	.and()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                    .antMatchers("/api/**/**")
                        .permitAll()
                    .antMatchers("/mmrs-notification/**")
                        .permitAll() 
//                    .antMatchers("/api/assetcategories/**", "/api/users/**")
//                        .permitAll()
//                    .antMatchers(HttpMethod.GET, "/api/regions/**", "/api/locations/**", "/api/assettypes/**").hasAnyAuthority("ADM")
//                    .antMatchers("/api/**").hasAnyRole("ADM","ROLE_ADM","USER")
                    .anyRequest()
                        .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
