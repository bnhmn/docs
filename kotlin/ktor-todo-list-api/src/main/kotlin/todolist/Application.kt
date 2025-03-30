package todolist

import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import org.slf4j.event.Level
import todolist.api.todoRoutes

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(CallLogging) { level = Level.INFO }
    install(ContentNegotiation) { json() }
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            log.error(cause.message, cause)
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<Throwable> { call, cause ->
            log.error(cause.message, cause)
            call.respond(InternalServerError)
        }
    }
    routing { todoRoutes() }
}
