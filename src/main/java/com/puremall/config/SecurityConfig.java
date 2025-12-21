package com.puremall.config;

/**
 * 安全配置类
 * 配置Spring Security的认证和授权规则
 */
import com.puremall.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            // 启用CORS支持，使用CorsConfig中定义的配置
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    new AntPathRequestMatcher("/api/**"),
                    // new AntPathRequestMatcher("/api/user/login"),
                    // new AntPathRequestMatcher("/api/user/register"),
                    // new AntPathRequestMatcher("/api/user/check-username"),
                    // new AntPathRequestMatcher("/api/user/check-email"),
                    // new AntPathRequestMatcher("/api/user/check-phone"),
                    new AntPathRequestMatcher("/swagger-ui.html"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/v3/api-docs/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            // 将JWT过滤器添加到UsernamePasswordAuthenticationFilter之前
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}