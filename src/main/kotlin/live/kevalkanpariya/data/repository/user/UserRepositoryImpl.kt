package live.kevalkanpariya.data.repository.user


import io.ktor.server.application.*
import live.kevalkanpariya.data.models.User
import live.kevalkanpariya.data.requests.UpdateProfileRequest
import live.kevalkanpariya.data.responses.UserResponseItem
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserRepositoryImpl(db: CoroutineDatabase): UserRepository {

    private val users = db.getCollection<User>()
    override suspend fun getUserInfos(page: Int, pageSize: Int): List<User>? {
        val userItems = users.find()
            .skip(page*pageSize)
            .limit(pageSize)
            .toList()
            /*.map { user ->
                val isFollowing = followsByUser.find { user.followedUserId == user.id } != null
                UserResponseItem(
                    userId = user.id,
                    profilePictureUrl = user.profileImageUrl,
                    username = user.username,
                    bio = user.bio,
                    name = user.profileName?: "Empty Name",
                    isFollowing =
                )
            }*/

        return userItems
    }

    override suspend fun createUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun deleteUser(userId: String): Boolean {
        return users.deleteOne(User::id eq userId).wasAcknowledged()
    }

    override suspend fun getUserById(id: String): User? {

        return users.findOne(User::id eq id)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest?,
        app: Application,
    ): Boolean {
        val user = getUserById(userId) ?: return false
        return users.updateOne(
            filter = User::id eq userId,
            update = combine(
                setValue(User::username, updateProfileRequest?.username),
                setValue(User::faceBookUrl, updateProfileRequest?.faceBookUrl),
                setValue(User::bio, updateProfileRequest?.bio),
                setValue(User::profileImageUrl, profileImageUrl?: user.profileImageUrl),
                setValue(User::bannerUrl, bannerUrl?: user.bannerUrl),
                setValue(User::faceBookUrl, updateProfileRequest?.faceBookUrl),
                setValue(User::instagramUrl, updateProfileRequest?.instagramUrl),
                setValue(User::twitterUrl, updateProfileRequest?.twitterUrl),
            )
        ).wasAcknowledged()


    }

    override suspend fun updatePasswordForUser(userId: String, password: String, salt: String): Boolean {
        return users.updateOne(
            filter = User::id eq userId,
            update = combine(
                setValue(User::password, password),
                setValue(User::salt, salt)
            )
        ).wasAcknowledged()
    }


    override suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        return users.findOneById(userId)?.email == email
    }

    override suspend fun searchForUsers(query: String): List<User> {
        return users.find(
            or(
                User::username regex Regex("(?i).*$query.*"),
                User::email eq query
            )
        )
            .descendingSort(User::followerCount)
            .toList()
    }

    override suspend fun getUsers(userIds: List<String>): List<User> {
        return users.find(User::id `in` userIds).toList()
    }
}