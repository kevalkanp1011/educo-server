package live.kevalkanpariya.service

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.User
import live.kevalkanpariya.data.models.UserHeader
import live.kevalkanpariya.data.repository.follow.FollowRepository
import live.kevalkanpariya.data.repository.user.UserRepository
import live.kevalkanpariya.data.requests.CreateAccountRequest
import live.kevalkanpariya.data.requests.UpdateProfileRequest
import live.kevalkanpariya.data.responses.ProfileResponse
import live.kevalkanpariya.data.responses.UserResponseItem
import live.kevalkanpariya.util.Constants
import live.kevalkanpariya.util.security.hashing.HashingService

class UserService (
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    suspend fun doesUserWithEmailExist(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun getUserHeaderProfile(userId: String): UserHeader? {
        val user = userRepository.getUserById(userId) ?: return null
        return UserHeader(
            name = user.profileName,
            profilePictureUrl = user.profileImageUrl
        )
    }

    suspend fun getUserInfos(
        ownUserId: String,
        page: Int,
        pageSize: Int
    ): List<UserResponseItem>? {
        val users = userRepository.getUserInfos(page, pageSize)?: return null
        val followsByUser = followRepository.getFollowsByUser(ownUserId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                userId = user.id,
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                name = user.profileName?: "empty",
                isFollowing = isFollowing
            )
        }.filter { it.userId != ownUserId }
    }

    suspend fun getUserProfile(userId: String, callerUserId: String): ProfileResponse? {
        val user = userRepository.getUserById(userId) ?: return null
        return ProfileResponse(
            userId = user.id,
            username = user.username,
            bio = user.bio,
            followerCount = user.followerCount,
            followingCount = user.followingCount,
            projectCount = user.totalProjectsCount,
            enrolledCourseCount = user.courseCount,
            name = user.profileName?: "Blank UserName",
            profilePictureUrl = user.profileImageUrl,
            bannerUrl = user.bannerUrl,
            faceBookUrl = user.faceBookUrl,
            instagramUrl = user.instagramUrl,
            twitterUrl = user.twitterUrl,
            isOwnProfile = userId == callerUserId,
            isFollowing = if (userId != callerUserId) {
                followRepository.doesUserFollow(callerUserId, userId)
            } else {
                false
            }
        )
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    suspend fun getUserById(userId: String): User? {
        return userRepository.getUserById(userId)
    }

    suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest?,
        app: Application
    ): Boolean {
        return userRepository.updateUser(userId, profileImageUrl, bannerUrl, updateProfileRequest, app)
    }

    suspend fun updatePasswordForUser(
        userId: String,
        hash: String,
        salt: String
    ): Boolean {

        return userRepository.updatePasswordForUser(userId = userId, password = hash, salt = salt)
    }

    suspend fun deleteUser(
        userId: String
    ): Boolean {
        return userRepository.deleteUser(userId)
    }

    suspend fun searchForUsers(query: String, userId: String): List<UserResponseItem> {
        val users = userRepository.searchForUsers(query)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                userId = user.id,
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                name = user.profileName?: "empty",
                isFollowing = isFollowing
            )
        }.filter { it.userId != userId }
    }

    suspend fun createUser(request: CreateAccountRequest, hashingService: HashingService) {
        val saltedHash = hashingService.generateSaltedHash(request.password)
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = saltedHash.hash,
                salt = saltedHash.salt,
                profileImageUrl = Constants.DEFAULT_PROFILE_PICTURE_PATH,
                bannerUrl = Constants.DEFAULT_BANNER_IMAGE_PATH,
                bio = "",
                faceBookUrl = null,
                instagramUrl = null,
                twitterUrl = null,
                userType = request.accountType
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object Success : ValidationEvent()
    }
}