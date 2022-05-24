package grails.springfox.sample

class BootStrap {

    def init = { servletContext ->
        Genre.withTransaction {
            ['Rock', 'Pop', 'Metal', 'Folk'].each {
                new Genre(name: it).save(flush: true)
            }

            new Artist(name: 'John', isBand: false,
                       signedTo: new Label(name: 'name', address: 'address').save() ).save()
        }
    }

    def destroy = {
    }
}
