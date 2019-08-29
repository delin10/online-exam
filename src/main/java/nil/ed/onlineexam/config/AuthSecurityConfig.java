package nil.ed.onlineexam.config;

import nil.ed.onlineexam.interceptor.CustomFilterSecurityInterceptor;
import nil.ed.onlineexam.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager(){
        return new AccessDecisionManagerImpl();
    }

    @Bean
    public SecurityMetadataSource securityMetadataSource(){
        return new InvocationSecurityMetadataSourceServiceImpl();
    }

    @Bean
    public CustomPasswordEncoder customPasswordEncoder(){
        return new CustomPasswordEncoder();
    }

    @Bean
    public CustomFilterSecurityInterceptor customFilterSecurityInterceptor(){
        CustomFilterSecurityInterceptor customFilterSecurityInterceptor =  new CustomFilterSecurityInterceptor(securityMetadataSource());
        customFilterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        return customFilterSecurityInterceptor;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(customPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers("/exam/login.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/exam/login.html")
                .loginProcessingUrl("/exam/login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler())
                .permitAll()
                .and()
                .csrf().disable();
    }
}
