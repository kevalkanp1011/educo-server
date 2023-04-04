package live.kevalkanpariya.service

import io.ktor.server.application.*
import live.kevalkanpariya.data.repository.follow.FollowRepository
import live.kevalkanpariya.data.repository.like.LikeRepository
import live.kevalkanpariya.data.repository.user.UserRepository
import live.kevalkanpariya.data.responses.UserResponseItem

class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int, app: Application): Boolean {
        return likeRepository.likeParent(userId, parentId, parentType, app)
    }

    suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.unlikeParent(userId, parentId, parentType)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikesForParent(parentId)
    }

    suspend fun getUsersWhoLikedParent(parentId: String, userId: String): List<UserResponseItem> {
        val userIds = likeRepository.getLikesForParent(parentId).map { it.userId }
        val users = userRepository.getUsers(userIds)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                userId = user.id,
                username = user.username,
                name = user.profileName?: "empty",
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }
}