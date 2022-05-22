package springfox.documentation.grails.definitions;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.util.Optional;

class TransformedProperty {
    protected final TypeDescription typeDescription;
    protected final DynamicType auxiliaryType;

    protected final String name;

    TransformedProperty(TypeDescription typeDescription, DynamicType auxiliaryType, String name) {
        this.typeDescription = typeDescription;
        this.auxiliaryType = auxiliaryType;
        this.name = name;
    }

    TransformedProperty(TypeDescription typeDescription, String name) {
        this.typeDescription = typeDescription;
        this.name = name;
        this.auxiliaryType = null;
    }

    Optional<DynamicType> auxiliaryType() {
        return Optional.ofNullable(auxiliaryType);
    }

    public String getName() {
        return name;
    }
}
