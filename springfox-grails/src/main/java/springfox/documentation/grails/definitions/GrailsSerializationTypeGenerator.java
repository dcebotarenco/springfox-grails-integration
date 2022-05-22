package springfox.documentation.grails.definitions;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.model.PersistentProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GrailsSerializationTypeGenerator {

    private final GrailsPropertySelector propertySelector;
    private final GrailsPropertyTransformer propertyTransformer;
    private final GeneratedClassNamingStrategy naming;

    @Autowired
    public GrailsSerializationTypeGenerator(GrailsPropertySelector propertySelector, GrailsPropertyTransformer propertyTransformer, GeneratedClassNamingStrategy naming) {
        this.propertySelector = propertySelector;
        this.propertyTransformer = propertyTransformer;
        this.naming = naming;
    }

    public Class<?> from(PersistentEntity domain) {
        DynamicType.Builder<Object> builder = new ByteBuddy().subclass(Object.class).name(naming.name(domain.getJavaClass()));
        List<DynamicType> auxiliaryTypes = new ArrayList<>();

        for (PersistentProperty property : getEntityProperties(domain)) {
            if (propertySelector.test(property)) {
                TransformedProperty transformedProperty = propertyTransformer.apply(property);
                transformedProperty.auxiliaryType().ifPresent(auxiliaryTypes::add);
                builder = builder.defineProperty(transformedProperty.getName(), transformedProperty.typeDescription);
            }
        }

        return builder.make().include(auxiliaryTypes)
            .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
    }

    private List<PersistentProperty> getEntityProperties(PersistentEntity domain) {
        List<PersistentProperty> persistentProperties = new ArrayList<>(domain.getPersistentProperties());
        persistentProperties.add(domain.getIdentity());
        return persistentProperties;
    }

}
