package springfox.documentation.grails.config;

import grails.core.GrailsApplicationLifeCycleAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;

import java.util.Map;

@Component
public class GrailsDocumentationPluginsLifeCycle extends GrailsApplicationLifeCycleAdapter {

    private static final Logger log = LoggerFactory.getLogger(GrailsDocumentationPluginsLifeCycle.class);
    private final DocumentationPluginsBootstrapper documentationPluginsBootstrapper;

    public GrailsDocumentationPluginsLifeCycle(DocumentationPluginsBootstrapper documentationPluginsBootstrapper) {
        this.documentationPluginsBootstrapper = documentationPluginsBootstrapper;
    }

    @Override
    public void onStartup(Map<String, Object> event) {
        log.info("Starting {}", GrailsDocumentationPluginsBootstrapper.class);
        documentationPluginsBootstrapper.start();
    }
}
