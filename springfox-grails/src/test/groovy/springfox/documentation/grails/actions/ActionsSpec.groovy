package springfox.documentation.grails.actions

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import org.grails.datastore.mapping.model.PersistentEntity
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Unroll
import springfox.documentation.grails.Actions
import springfox.documentation.grails.GrailsActionContext
import springfox.documentation.grails.doubles.AController
import springfox.documentation.grails.doubles.GrailsControllerSupport

class ActionsSpec extends ActionSpecificationFactorySpec implements GrailsControllerSupport {
  def "Cannot instantiate this class"() {
    when:
      new Actions()
    then:
      thrown(UnsupportedOperationException)
  }

  def "Detects grails actions"() {
    given:
      def controller = aController()
    when:
      def handlerMethods = Actions.actionsToHandler(controller)
    then:
      handlerMethods.size() == 9
      handlerMethods.containsKey("index")
      handlerMethods.containsKey("show")
      handlerMethods.containsKey("create")
      handlerMethods.containsKey("update")
      handlerMethods.containsKey("save")
      handlerMethods.containsKey("edit")
      handlerMethods.containsKey("patch")
      handlerMethods.containsKey("delete")
      handlerMethods.containsKey("other")
  }

  @Unroll
  def "Detects grails method overrides for #action"() {
    given:
      def grailsController = grailsController(controller)
      def context = context(grailsController, action)
    when:
      def methods = Actions.methodOverrides(context)
    then:
      methods == expected
    where:
      controller          | action          | expected
      OverriddenController | "withOverrides" | [RequestMethod.POST] as Set
      OverriddenController | "noOverrides"   | [] as Set
  }

  @Unroll
  def "Detects grails method overrides with defaults for #action"() {
    given:
      def grailsController = grailsController(controller)
      def context = context(grailsController, action)
    when:
      def methods = Actions.methodOverrides(context, [RequestMethod.GET] as Set)
    then:
      methods == expected
    where:
      controller          | action          | expected
      OverriddenController | "withOverrides" | [RequestMethod.POST] as Set
      OverriddenController | "noOverrides"   | [RequestMethod.GET] as Set
  }

  @Unroll
  def "Detects grails produces overrides for #controller"() {
    given:
      def grailsController = grailsController(controller)
      def context = context(grailsController, "any")
    when:
      def methods = Actions.mediaTypeOverrides(context)
    then:
      methods == expected
    where:
      controller          | expected
    AController | [MediaType.APPLICATION_JSON] as Set
      OverriddenController | [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML] as Set
  }

  def grailsController(Class controller) {
    def grails = Stub(GrailsControllerClass)
    grails.clazz >> controller
    grails
  }

    GrailsActionContext context(controller, action) {
    new GrailsActionContext(
        controller,
        Stub(PersistentEntity),
        actionAttributes,
        action,
        new TypeResolver())
  }

  class OverriddenController {
    static allowedMethods = [withOverrides: "POST", update: "PUT", delete: "DELETE"]
    static responseFormats = ['json', 'xml']
    
    def withOverrides() {
    }

    def show() {
    }
  }
}
