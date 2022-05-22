package springfox.documentation.grails.definitions;

import org.grails.datastore.mapping.model.PersistentProperty;

import java.util.ArrayList;
import java.util.List;

public class DefaultGrailsPropertySelector implements GrailsPropertySelector {

    private static final List<String> forbiddenProperties = new ArrayList<>();

    static {
        forbiddenProperties.add("version");
        forbiddenProperties.add("dirty");
        forbiddenProperties.add("dirtyPropertyNames");
        forbiddenProperties.add("errors");
        forbiddenProperties.add("attached");
    }

    @Override
    public boolean test(PersistentProperty each) {
        return forbiddenProperties.stream().noneMatch(n -> n.equals(each.getName()));
    }
}
