package com.practice.configure;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Log4j2
@Configuration
public class WebConfiguration implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    @Value("${attachment.path}")
    private String path;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // WebMvcConfigurer interface를 상속받아 addResourceHandlers method를 오버 라이딩하고
    // 리소스 등록 및 핸들러를 관리하는 객체인 ResourceHandlerRegistry를 통해 리소스의 위치와 리소스와 매칭 될 url을 설정합니다.

    // addResourceHandler : 리소스와 연결될 URL path를 지정합니다. (클라이언트가 파일에 접근하기 위해 요청하는 url)
    // addResourceLocations: 실제 리소스가 존재하는 외부 경로를 지정합니다.
    // 경로의 마지막은 반드시 " / "로 끝나야 하고, 로컬 디스크 경로일 경우 file:/// 접두어를 꼭 붙여야 합니다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/**") // html에서 upload 패턴이 나오면 아래있는 파일 Resource 발동
                .addResourceLocations("file://" + path)
                .setCachePeriod(60 * 10 * 6)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
