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

    /**
     * This constructor performs injection on this instance,
     * based on the current web application context.
     * <p>Intended for use as a base class.
     * @see #processInjectionBasedOnCurrentContext
     */
    public SpringBootBeanAutowiringSupport() {
        processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Process {@code @Autowired} injection for the given target object,
     * based on the current web application context.
     * <p>Intended for use as a delegate.
     * @param target the target object to process
     * @see org.springframework.web.context.ContextLoader#getCurrentWebApplicationContext()
     */
    public static void processInjectionBasedOnCurrentContext(Object target) {
        Assert.notNull(target, "Target object must not be null");
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
