package live.kevalkanpariya.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun Application.configureAuth() {

    install(Authentication) {

        jwt("auth-eduCo") {
            val jwtAudience = this@configureAuth.environment.config.property("jwt.audience").getString()
            val jwtSecret = System.getenv("JWT_SECRET")
            realm = this@configureAuth.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(this@configureAuth.environment.config.property("jwt.domain").getString())
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }

        }

    }
}

val JWTPrincipal.userId: String?
    get() = getClaim("userId", String::class)
