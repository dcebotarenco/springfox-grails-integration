package springfox.documentation.grails.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;


/**
 * Disable the original {@link springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper} by setting the
 * <b>springfox.documentation.auto-startup</b> to {@link Boolean#FALSE}
 */
public class SpringDocumentationPluginsPostProcessor implements EnvironmentPostProcessor {

    private static final DeferredLog log = new DeferredLog();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        application.addInitializers(context -> log.replayTo(SpringDocumentationPluginsPostProcessor.class));
        log.info("Disable original springfox documentation auto-startup to favor Grails spring fox implementation");
        Map<String, Object> springFoxProperties = new HashMap<>();
        springFoxProperties.put("springfox.documentation.auto-startup", Boolean.FALSE);
        environment.getPropertySources().addLast(new MapPropertySource("spring-fox", springFoxProperties));
    }
}
