package springfox.documentation.grails

import grails.web.mapping.UrlMapping
import org.grails.web.mapping.DefaultUrlMappingEvaluator

trait UrlMappingSupport {
  def allMappings = "mappings = {\n" +
  '  delete \"/$controller/$id(.$format)?\"(action: \"delete\")\n' +
  '  get \"/$controller(.$format)?\"(action: \"index\")\n' +
  '  get \"/$controller/$id(.$format)?\"(action: \"show\")\n' +
  '  get \"/$controller/$id/edit\"(action: \"edit\")\n' +
  '  post \"/$controller(.$format)?\"(action: \"save\")\n' +
  '  put \"/$controller/$id(.$format)?\"(action: \"update\")\n' +
  '  patch \"/$controller/$id(.$format)?\"(action: \"patch\")\n' +
  "\n" +
  "  \"/\"(controller: 'application', action: 'index')\n" +
  "  \"500\"(view: '/error')\n" +
  "  \"404\"(view: '/notFound')\n" +
  "\n" +
  "  //for testing\n" +
  "  group \"/store\", {\n" +
  "    group \"/product\", {\n" +
  '      get \"/$id\"(controller: \"product\")\n' +
  "    }\n" +
  "  }\n" +
  "  group \"/product\", {\n" +
  "    \"/apple\"(controller: \"product\", id: \"apple\")\n" +
  "    \"/htc\"(controller: \"product\", id: \"htc\")\n" +
  "  }\n" +
  "  get \"/product\"(controller: \"product\", action: \"list\")\n" +
  "  \"/books\"(resources: 'book')\n" +
  "  \"/albums\"(resources: 'album', excludes: ['delete', 'update'])\n" +
  "  \"/artists\"(resources: 'artist', includes: ['index', 'show'])\n" +
  "  \"/novels\"(resources: 'novel') {\n" +
  "    \"/authors\"(resources: \"author\")\n" +
  "  }\n" +
  "}"

  List<UrlMapping> urlMappings(String mappings = allMappings) {
    GroovyShell shell = new GroovyShell()
    Binding binding = new Binding()
    Script script = shell.parse(mappings)

    script.setBinding(binding)
    script.run()

    Closure closure = (Closure)binding.getVariable("mappings")
    new DefaultUrlMappingEvaluator(null).evaluateMappings(closure)
  }
}