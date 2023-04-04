package live.kevalkanpariya.service

import io.ktor.server.application.*
import live.kevalkanpariya.data.repository.follow.FollowRepository
import live.kevalkanpariya.data.requests.FollowUpdateRequest

class FollowService(private val followRepository: FollowRepository) {
    suspend fun followUserIfExists(followedUserId: String, followingUserId: String, app: Application): Boolean {
        return followRepository.followUserIfExists(
            followingUserId,
            followedUserId,
            app
        )
    }

    suspend fun unfollowUserIfExists(followedUserId: String, followingUserId: String): Boolean {
        return followRepository.unfollowUserIfExists(
            followingUserId,
            followedUserId
        )
    }

    suspend fun doesUserFollows(followingUserId: String, followedUserId: String): Boolean {
        return followRepository.doesUserFollow(followingUserId, followedUserId)
    }
}