package live.kevalkanpariya.data.requests

data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val faceBookUrl: String,
    val instagramUrl: String,
    val twitterUrl: String,
)
