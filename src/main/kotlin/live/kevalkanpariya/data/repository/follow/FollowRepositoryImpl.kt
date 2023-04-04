package live.kevalkanpariya.data.repository.follow

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Following
import live.kevalkanpariya.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.inc

class FollowRepositoryImpl(
    db: CoroutineDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String,
        app: Application
    ): Boolean {
        val doesFollowingUserExist = users.findOne(filter = User::id eq followingUserId) != null
        val doesFollowedUserExist = users.findOne(filter = User::id eq followingUserId) != null
        if(!doesFollowingUserExist || !doesFollowedUserExist) {
            return false
        }
        val up1 = users.updateOne(
            filter = User::id eq followingUserId,
            update = inc(User::followingCount, 1)
        ).wasAcknowledged()
        app.log.info("updated user $up1")
        val up2 = users.updateOne(
            filter = User::id eq followedUserId,
            update = inc(User::followerCount, 1)
        ).wasAcknowledged()
        app.log.info("updated user $up1")
        if (up1 && up2) {
            following.insertOne(
                Following(followedUserId = followedUserId, followingUserId = followingUserId)
            )
        }
        return true
    }

    override suspend fun unfollowUserIfExists(followingUserId: String, followedUserId: String): Boolean {
        val doesFollowingUserExist = users.findOne(filter = User::id eq followingUserId) != null
        val doesFollowedUserExist = users.findOne(filter = User::id eq followingUserId) != null
        if(!doesFollowingUserExist || !doesFollowedUserExist) {
            return false
        }
        val deleteResult = following.deleteOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId,
            )
        )
        if(deleteResult.deletedCount > 0) {
            val ua1 = users.updateOne(
                filter = User::id eq followingUserId,
                inc(User::followingCount, -1)
            ).wasAcknowledged()
            val ua2 = users.updateOne(
                filter = User::id eq followedUserId,
                update = inc(User::followerCount, -1)
            ).wasAcknowledged()

            if (!ua1 or !ua2)
                return false
        }
        return deleteResult.deletedCount > 0
    }

    override suspend fun doesUserFollow(followingUserId: String, followedUserId: String): Boolean {
        return following.findOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId
            )
        ) != null
    }

    override suspend fun getFollowsByUser(userId: String): List<Following> {
        return following.find(Following::followingUserId eq userId).toList()
    }
}