package config.initializer;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.publiccms.common.constants.CommonConstants;

import config.spring.CmsConfig;

/**
 * CMS初始化
 * 
 * Management Initializer
 *
 */
public class CmsInitializer extends AbstractContextLoaderInitializer implements WebApplicationInitializer {// 防止jetty等追求速度的容器不扫描父类实现的接口

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
        CommonConstants.applicationContext = rootAppContext;
        rootAppContext.register(CmsConfig.class);
        return rootAppContext;
    }
}
