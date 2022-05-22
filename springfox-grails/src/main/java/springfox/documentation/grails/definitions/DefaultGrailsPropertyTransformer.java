package springfox.documentation.grails.definitions;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.datastore.mapping.model.types.ToOne;

public class DefaultGrailsPropertyTransformer implements GrailsPropertyTransformer {

    @Override
    public TransformedProperty apply(PersistentProperty property) {
        String name = property.getName();
        if (property instanceof ToOne) {
            ToOne<?> toOneRelation = (ToOne<?>) property;
            DynamicType.Unloaded<Object> toOneType = createIdType(toOneRelation);
            return new TransformedProperty(toOneType.getTypeDescription(), toOneType, name);
        }

        return new TransformedProperty(TypeDescription.ForLoadedType.of(property.getType()), name);
    }

    protected DynamicType.Unloaded<Object> createIdType(ToOne<?> domain) {
        String name = domain.getAssociatedEntity().getIdentity().getName();
        Class<?> clazz = domain.getAssociatedEntity().getIdentity().getType();
        String packageName = domain.getAssociatedEntity().getJavaClass().getPackage().getName();
        DynamicType.Builder<Object> builder = new ByteBuddy()
            .subclass(Object.class)
            .name(String.format("%s.generated.%s", packageName, "Id"))
            .defineProperty(name, clazz);
        return builder.make();
    }
}
