package grails.springfox.sample


import org.springframework.beans.factory.annotation.Autowired
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper

class BootStrap {

    @Autowired
    DocumentationPluginsBootstrapper bootstrapper

    def init = { servletContext ->
        Genre.withTransaction {
            ['Rock', 'Pop', 'Metal', 'Folk'].each {
                new Genre(name: it).save(flush: true)
            }

            new Artist(name: 'John', isBand: false,
                       signedTo: new Label(name: 'name', address: 'address').save() ).save()
        }
        bootstrapper.start()
    }
    def destroy = {
    }
}
