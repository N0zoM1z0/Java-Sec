package config.spring;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.publiccms.common.handler.FullBeanNameGenerator;
import com.publiccms.common.interceptor.CsrfInterceptor;
import com.publiccms.common.interceptor.SiteInterceptor;
import com.publiccms.common.interceptor.WebContextInterceptor;
import com.publiccms.common.view.DefaultWebFreeMarkerView;
import com.publiccms.common.view.WebFreeMarkerView;
import com.publiccms.common.view.WebFreeMarkerViewResolver;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;

import jakarta.annotation.Resource;

/**
 * 
 * WebConfig WebServlet配置类
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.publiccms.controller.web", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(value = { Controller.class }) }, nameGenerator = FullBeanNameGenerator.class)
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private WebContextInterceptor webInterceptor;
    @Resource
    private SiteInterceptor siteInterceptor;
    @Resource
    private CacheComponent cacheComponent;

    @Bean
    public LocaleResolver localeResolver(Environment env) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver("PUBLICCMS_LOCALE");
        localeResolver.setCookieMaxAge(Duration.ofDays(30));
        String defaultLocale = env.getProperty("cms.defaultLocale");
        if (!"auto".equalsIgnoreCase(defaultLocale)) {
            localeResolver.setDefaultLocale(Locale.forLanguageTag(defaultLocale));
        }
        return localeResolver;
    }

    /**
     * 视图层解析器
     * 
     * @return default web viewresolver
     */
    @Bean
    public ViewResolver defaultWebViewResolver() {
        FreeMarkerViewResolver bean = new FreeMarkerViewResolver();
        bean.setOrder(1);
        bean.setViewClass(DefaultWebFreeMarkerView.class);
        bean.setPrefix("/web/");
        bean.setContentType("text/html;charset=UTF-8");
        bean.setExposeSpringMacroHelpers(false);
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 视图层解析器
     * 
     * @param templateComponent
     * @return web viewresolver
     */
    @Bean
    public ViewResolver webViewResolver(TemplateComponent templateComponent) {
        WebFreeMarkerViewResolver bean = new WebFreeMarkerViewResolver();
        bean.setOrder(0);
        bean.setConfiguration(templateComponent.getWebConfiguration());
        bean.setViewClass(WebFreeMarkerView.class);
        bean.setContentType("text/html;charset=UTF-8");
        bean.setExposeSpringMacroHelpers(false);
        cacheComponent.registerCachingViewResolverList(bean);
        return bean;
    }

    /**
     * 拦截器
     * 
     * @return web servlet interceptor
     */
    @Bean
    public WebContextInterceptor webInterceptor() {
        return new WebContextInterceptor();
    }

    @Bean
    public CsrfInterceptor csrfInterceptor() {
        return new CsrfInterceptor(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(csrfInterceptor());
        registry.addInterceptor(webInterceptor);
        registry.addInterceptor(siteInterceptor);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                List<MediaType> list = new ArrayList<>();
                list.add(MediaType.TEXT_PLAIN);
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(list);
                SimpleModule module = new SimpleModule();
                module.addSerializer(Long.class, ToStringSerializer.instance);
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper().registerModule(module);
            }
        }
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPathMatcher(new AntPathMatcher());
    }
}
