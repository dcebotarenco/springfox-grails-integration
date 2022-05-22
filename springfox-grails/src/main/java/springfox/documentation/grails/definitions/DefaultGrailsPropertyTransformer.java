package springfox.documentation.grails.definitions;

import org.grails.datastore.mapping.model.PersistentProperty;
import springfox.documentation.builders.AlternateTypePropertyBuilder;

public class DefaultGrailsPropertyTransformer implements GrailsPropertyTransformer {
    @Override
    public AlternateTypePropertyBuilder apply(PersistentProperty property) {
        Class<?> type = property.getType();
        return new AlternateTypePropertyBuilder()
            .withName(property.getName())
            .withType(type)
            .withCanRead(true)
            .withCanWrite(true);
    }
}
