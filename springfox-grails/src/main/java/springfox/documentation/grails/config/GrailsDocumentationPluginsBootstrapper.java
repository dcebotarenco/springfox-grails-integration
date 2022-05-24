package springfox.documentation.grails.config;


import com.fasterxml.classmate.TypeResolver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;

import javax.servlet.ServletContext;
import java.util.List;

@Component
public class GrailsDocumentationPluginsBootstrapper extends DocumentationPluginsBootstrapper {

    GrailsDocumentationPluginsBootstrapper(DocumentationPluginsManager documentationPluginsManager,
                                           List<RequestHandlerProvider> handlerProviders,
                                           DocumentationCache scanned,
                                           ApiDocumentationScanner resourceListing,
                                           TypeResolver typeResolver,
                                           Defaults defaults,
                                           ServletContext servletContext,
                                           Environment environment) {
        super(documentationPluginsManager, handlerProviders, scanned, resourceListing,
            typeResolver, defaults, servletContext, environment);

    }

    /**
     * Disable {@link org.springframework.context.SmartLifecycle}
     */
    @Override
    public boolean isAutoStartup() {
        return false;
    }

}