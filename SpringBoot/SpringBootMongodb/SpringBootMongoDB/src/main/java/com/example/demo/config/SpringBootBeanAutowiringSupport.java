package com.example.demo.config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author linzhiqiang
 * @create 2018/08/01
 */
public class SpringBootBeanAutowiringSupport {
    private static final Log logger = LogFactory.getLog(SpringBootBeanAutowiringSupport.class);

    public SpringBootBeanAutowiringSupport() {
        processInjectionBasedOnCurrentContext(this);
    }

    public static void processInjectionBasedOnCurrentContext(Object target) {
        WebApplicationContext cc = WebApplicationContextLocator.getCurrentWebApplicationContext();
        if (cc != null) {
            AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
            bpp.setBeanFactory(cc.getAutowireCapableBeanFactory());
            bpp.processInjection(target);
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("Current WebApplicationContext is not available for processing of " +
                        ClassUtils.getShortName(target.getClass()) + ": " +
                        "Make sure this class gets constructed in a Spring web application. Proceeding without injection.");
            }
        }
    }
}
