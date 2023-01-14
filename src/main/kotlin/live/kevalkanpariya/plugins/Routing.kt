package live.kevalkanpariya.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.repository.UserDataSource
import live.kevalkanpariya.routes.*
import live.kevalkanpariya.routes.auth.*

import live.kevalkanpariya.routes.aws_s3.addModuleImage
import live.kevalkanpariya.storage.exposed.LocalSource
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase


fun Application.configureRouting() {

    routing {
        val userDataSource by inject<UserDataSource>()
        //val courseRepo: CourseRepo by inject(Cou)rseRepo::class.java)
        val localSource by inject<LocalSource>()


        rootRoute()
        /* Authentication Routes */
        tokenVerification(application, userDataSource)
        getUserInfoRoute(application, userDataSource)
        updateUserInfo(application, userDataSource)
        deleteUser(application, userDataSource)
        signOutRoute()
        authorizedRoute()
        unauthorizedRoute()
        /*Course Routes*/
        //getCourse(courseRepo)
        //createCourse(courseRepo)
        //updateCourse(courseRepo)
        //deleteCourse(courseRepo)

        addModuleImage(localSource)


    }
}
