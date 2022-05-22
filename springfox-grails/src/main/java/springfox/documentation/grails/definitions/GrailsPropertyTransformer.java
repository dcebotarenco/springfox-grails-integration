package springfox.documentation.grails.definitions;

import org.grails.datastore.mapping.model.PersistentProperty;

import java.util.function.Function;

public interface GrailsPropertyTransformer extends Function<PersistentProperty, TransformedProperty> {
}
