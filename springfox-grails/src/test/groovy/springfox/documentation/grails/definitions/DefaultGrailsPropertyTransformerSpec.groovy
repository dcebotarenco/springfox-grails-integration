package springfox.documentation.grails.definitions


import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.types.Identity
import org.grails.datastore.mapping.model.types.Simple
import org.grails.datastore.mapping.model.types.ToOne
import spock.lang.Specification
import spock.lang.Unroll

class DefaultGrailsPropertyTransformerSpec extends Specification {
    @Unroll
    def "Infers types correctly for grails property #name"() {
        given:
        def sut = new DefaultGrailsPropertyTransformer()
        when:
        def transformed = sut.apply(property)
        then:
        transformed.name == name
        transformed.typeDescription.name == type
        boolean isPresent = transformed.auxiliaryType != null
        isPresent == expectedAuxiliaryTypePresent
        where:
        property                       || name            | type                                                      | expectedAuxiliaryTypePresent
        id()                           || "id"            | Long.class.name                                           | false
        scalarProperty("test",
                       String)         || "test"          | String.class.name                                         | false
        relatedToOneEntityProperty()   || "relatedEntity" | "springfox.documentation.grails.definitions.generated.Id" | true
    }

    def id() {
        def property = Stub(Identity)
        property.type >> Long
        property.name >> "id"
        property
    }

    def scalarProperty(propertyName, propertyType) {
        def property = Stub(Simple)
        property.type >> propertyType
        property.name >> propertyName
        property
    }

    def relatedToOneEntityProperty() {
        ToOne property = Stub(ToOne)
        property.associatedEntity >> domainClass(RelatedEntity)
        property.name >> "relatedEntity"
        property.getOwner() >> relatedEntityDomain()
        property
    }


    PersistentEntity domainClass(Class clazz) {
        PersistentEntity domain = Stub(PersistentEntity)
        domain.javaClass >> clazz
        domain.identity >> id()
        return domain
    }

    PersistentEntity relatedEntityDomain() {
        def domain = Stub(PersistentEntity)
        domain.identity >> id()
        domain
    }

    class RelatedEntity {
    }
}
