package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappings
import org.grails.datastore.mapping.model.PersistentEntity
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification
import spock.lang.Unroll
import springfox.documentation.grails.doubles.AController

class GrailsActionAttributesSpec extends Specification {

    @Unroll
    def "Gets the http methods for operations correctly"() {
        given:
        def sut = actionAttributes(linkGenerator(), urlMappings())
        when:
        def actual = sut.httpMethods(context)
        then:
        actual == methods
        where:
        context         | methods
        noOverrides()   | [] as Set
        withOverrides() | [RequestMethod.POST] as Set
    }

    GrailsActionContext noOverrides() {
        context(noOverridesController(), "noOverride")
    }

    GrailsActionContext withOverrides() {
        context(withOverridesController(), "withOverrides")
    }

    GrailsActionContext context(controller, action) {
        new GrailsActionContext(controller,
                                Stub(PersistentEntity),
                                actionAttributes(linkGenerator(), urlMappings()),
                                action,
                                new TypeResolver())
    }

    def noOverridesController() {
        controller(AController, "A")
    }

    def withOverridesController() {
        controller(OverriddenController, "Overridden")
    }

    def controller(controllerClazz, name) {
        def controller = Stub(GrailsControllerClass)
        controller.clazz >> controllerClazz
        controller.name >> name
        controller
    }

    def actionAttributes(linkGenerator, urlMappings) {
        new GrailsActionAttributes(
            linkGenerator,
            urlMappings)
    }

    UrlMappings urlMappings() {
        def mappings = Stub(UrlMappings)
        mappings.urlMappings >> [noOverrideMapping(), withOverrideMapping()]
        mappings
    }

    def noOverrideMapping() {
        def noOverride = Stub(UrlMapping)
        noOverride.actionName >> "noOverride"
        noOverride.httpMethod >> "GET"
        noOverride.controllerName >> "A"
        noOverride
    }


    def withOverrideMapping() {
        def noOverride = Stub(UrlMapping)
        noOverride.actionName >> "withOverride"
        noOverride.httpMethod >> "POST"
        noOverride.controllerName >> "Override"
        noOverride
    }

    LinkGenerator linkGenerator() {
        def links = Stub(LinkGenerator)
        links.link(_) >> "/test"
        links.serverBaseURL >> "http://localhost:8080"
        links
    }

    class OverriddenController {
        static allowedMethods = [withOverrides: "POST", update: "PUT", delete: "DELETE"]

        def withOverrides() {
        }
    }
}
