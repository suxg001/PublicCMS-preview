package config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.publiccms.common.interceptor.web.WebContextInterceptor;
import com.publiccms.common.view.WebFreeMarkerViewResolver;
import com.publiccms.common.view.web.WebFreeMarkerView;
import com.publiccms.logic.component.TemplateComponent;

/**
 * 
 * WebConfig WebServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.views.controller.web", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) })
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private WebContextInterceptor initializingInterceptor;

    /**
     * 视图层解析器
     * 
     * @return
     * @throws IOException
     */
    @Bean
    public WebFreeMarkerViewResolver viewResolver(TemplateComponent templateComponent) {
        WebFreeMarkerViewResolver bean = new WebFreeMarkerViewResolver();
        bean.setOrder(0);
        bean.setConfiguration(templateComponent.getWebConfiguration());
        bean.setViewClass(WebFreeMarkerView.class);
        bean.setContentType("text/html;charset=UTF-8");
        return bean;
    }

    /**
     * 拦截器
     * 
     * @return
     */
    @Bean
    public WebContextInterceptor initializingInterceptor() {
        return new WebContextInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initializingInterceptor);
    }
}
