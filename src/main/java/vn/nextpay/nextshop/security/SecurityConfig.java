package vn.nextpay.nextshop.security;

import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private StringRedisTemplate stringRedisTemplate;

//    public SecurityConfig(StringRedisTemplate stringRedisTemplate) {
//        this.stringRedisTemplate = stringRedisTemplate;
//    }

    public SecurityConfig() { }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .anyRequest().permitAll();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/swagger-ui.html", "/common/**",
                        "/v2/api-docs", "/swagger-resources/configuration/ui", "/configuration/security", "/configuration/ui",
                        "/swagger-resources", "/swagger-resources/configuration/security", "/webjars/**", "/actuator/**",
                        "/api/customers/**", "/api/partners/customers/create", "/healthcheck/**").permitAll()
                .anyRequest().authenticated() // Các request còn lại đều cần được authenticated
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
}
