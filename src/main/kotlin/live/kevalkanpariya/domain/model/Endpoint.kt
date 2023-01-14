package live.kevalkanpariya.domain.model

sealed class Endpoint(val path: String) {
    //Authentication Routes
    object Root: Endpoint(path = "/")
    object TokenVerification: Endpoint(path = "/token_verification")
    object GetUserInfo: Endpoint(path = "/get_user")
    object UpdateUserInfo: Endpoint(path = "/update_user")
    object DeleteUser: Endpoint(path = "/delete_user")
    object SignOut: Endpoint(path = "/sign_out")
    object Unauthorized: Endpoint(path = "/unauthorized")
    object Authorized: Endpoint(path = "/authorized")
    //Course Access Routes
    object GetCourse: Endpoint(path = "/get_course")
    object CreateCourse: Endpoint(path = "/create_course")
    object UpdateCourse: Endpoint(path = "/update_course")
    object DeleteCourse: Endpoint(path = "/delete_course")
    //Image Upload Route
    object ImageUpload: Endpoint(path = "/images/{Id}/image")
}