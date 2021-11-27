package solutions.desati.twatter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import solutions.desati.twatter.interceptors.AuthInterceptor;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    final AuthInterceptor authInterceptor;
    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns(
                "/status",
                "/details",
                "/feed",
                "/self",
                "/user/*/follow",
                "/user/*/unfollow",
                "/user/*/message",
                "/post",
                "/conversations/**"
        );
    }
}
