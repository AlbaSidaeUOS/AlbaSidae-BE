package albabe.albabe.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 리액트 서버 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 옵션 추가
                .allowedHeaders("*");
    }
}
