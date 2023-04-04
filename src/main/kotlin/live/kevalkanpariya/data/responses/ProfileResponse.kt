package live.kevalkanpariya.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val userId: String,
    val name: String,
    val username: String,
    val bio: String,
    val followerCount: Int,
    val followingCount: Int,
    val enrolledCourseCount: Int,
    val projectCount: Int,
    val profilePictureUrl: String,
    val bannerUrl: String?,
    val faceBookUrl: String?,
    val instagramUrl: String?,
    val twitterUrl: String?,
    val isOwnProfile: Boolean,
    val isFollowing: Boolean
)
