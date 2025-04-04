package springfox.documentation.grails.definitions

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsApplication
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.types.Identity
import org.grails.datastore.mapping.model.types.Simple
import spock.lang.Specification
import springfox.documentation.grails.doubles.Pet
import springfox.documentation.grails.naming.DefaultGeneratedClassNamingStrategy

class DefaultGrailsAlternateTypeRuleConventionSpec extends Specification {
    def "Alternate types for grails classes are created"() {
        given:
        def app = grailsApplication()
        def resolver = new TypeResolver()
        def sut = new DefaultGrailsAlternateTypeRuleConvention(resolver, app, grailsGenerator())
        when:
        def rules = sut.rules()
        and:
        def alternate = rules[0].alternateFor(resolver.resolve(Pet))
        then:
        rules.size() == 1
        alternate.erasedType.simpleName == "Pet"
        alternate.erasedType.package.name == "springfox.documentation.grails.doubles.generated"
        alternate.erasedType.declaredFields.find { it.name == "name" } != null
        alternate.erasedType.declaredMethods.find { it.name == "getName" } != null
        alternate.erasedType.declaredMethods.find { it.name == "setName" } != null
    }

    def grailsGenerator() {
        new GrailsSerializationTypeGenerator(
            new DefaultGrailsPropertySelector(),
            new DefaultGrailsPropertyTransformer(),
            new DefaultGeneratedClassNamingStrategy())
    }

    def grailsApplication() {
        def app = Stub(GrailsApplication)
        MappingContext mappingContext = Stub(MappingContext)
        app.getMappingContext() >> mappingContext
        mappingContext.getPersistentEntities() >> [petDomain()]
        app
    }

    def petDomain() {
        def domain = Stub(PersistentEntity)
        domain.name >> "Pet"
        domain.javaClass >> Pet
        domain.persistentProperties >> [property("name", String)]
        domain.identity >> identityProperty('id', Long)
        domain
    }

    def property(name, type) {
        def property = Stub(Simple)
        property.name >> name
        property.type >> type
        property
    }

    def identityProperty(String name, Class type) {
        def property = Stub(Identity)
        property.name >> name
        property.type >> type
        property
    }
}
