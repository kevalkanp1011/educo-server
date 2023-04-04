package live.kevalkanpariya.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.data.requests.CreateAccountRequest
import live.kevalkanpariya.data.requests.LoginRequest
import live.kevalkanpariya.data.requests.ResetPasswordRequest
import live.kevalkanpariya.data.responses.AuthResponse
import live.kevalkanpariya.data.responses.BasicApiResponse
import live.kevalkanpariya.service.UserService
import live.kevalkanpariya.util.ApiResponseMessages.FIELDS_BLANK
import live.kevalkanpariya.util.ApiResponseMessages.INVALID_CREDENTIALS
import live.kevalkanpariya.util.ApiResponseMessages.USER_ALREADY_EXISTS
import live.kevalkanpariya.util.security.hashing.HashingService
import live.kevalkanpariya.util.security.hashing.SaltedHash
import org.apache.commons.codec.digest.DigestUtils
import java.util.*

fun Route.createUser(userService: UserService, hashingService: HashingService) {
    post("/api/user/create") {
        val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (userService.doesUserWithEmailExist(request.email)) {
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = USER_ALREADY_EXISTS
                )
            )
            return@post
        }

        when (userService.validateCreateAccountRequest(request)) {
            is UserService.ValidationEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
            }
            is UserService.ValidationEvent.Success -> {
                userService.createUser(request, hashingService)
                call.respond(
                    BasicApiResponse<Unit>(successful = true)
                )
            }

            else -> {}
        }
    }
}

fun Route.loginUser(
    hashingService: HashingService,
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
            return@post
        }
        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (isValidPassword) {
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = AuthResponse(
                        userId = user.id,
                        token = token
                    )
                )
            )
        } else {
            println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password}")
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
            return@post
        }
    }
}

fun Route.resetPassword(userService: UserService, hashingService: HashingService) {
    authenticate("auth-eduCo") {
        post("/api/user/reset_password") {
            val request = call.receiveOrNull<ResetPasswordRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val user = userService.getUserById(call.userId)

            val isCorrectOldPassword = hashingService.verify(
                value = request.oldPassword,
                saltedHash = SaltedHash(
                    hash = user!!.password,
                    salt = user.salt
                )
            )

            if (!isCorrectOldPassword) {
                call.respond(
                    BasicApiResponse<Unit>(successful = false)
                )
                return@post
            }

            val saltedHash = hashingService.generateSaltedHash(request.newPassword)
            userService.updatePasswordForUser(call.userId, hash = saltedHash.hash, salt = saltedHash.salt)
            call.respond(
                BasicApiResponse<Unit>(successful = true)
            )
        }
    }
}

fun Route.authenticate() {
    authenticate("auth-eduCo") {
        get("/api/user/authenticate") {
            call.respond(HttpStatusCode.OK, message = "you are successfully authenticated")
        }
    }
}
