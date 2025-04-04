package springfox.documentation.grails.definitions;

import org.grails.datastore.mapping.model.PersistentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GrailsSerializationTypeGenerator {

    private final GrailsPropertySelector propertySelector;
    private final GrailsPropertyTransformer propertyTransformer;
    private final GeneratedClassNamingStrategy naming;

    @Autowired
    public GrailsSerializationTypeGenerator(GrailsPropertySelector propertySelector,
                                            GrailsPropertyTransformer propertyTransformer,
                                            GeneratedClassNamingStrategy naming) {
        this.propertySelector = propertySelector;
        this.propertyTransformer = propertyTransformer;
        this.naming = naming;
    }

    public Class<?> from(PersistentEntity domain) {
        List<AlternateTypePropertyBuilder> properties =
            Stream.concat(domain.getPersistentProperties().stream(),
                    Stream.of(domain.getIdentity()))
                .filter(propertySelector)
                .map(propertyTransformer)
                .collect(Collectors.toList());
        Class<?> newType = new AlternateTypeBuilder()
            .fullyQualifiedClassName(naming.name(domain.getJavaClass()))
            .withProperties(properties)
            .build();
        return newType;
    }
}
