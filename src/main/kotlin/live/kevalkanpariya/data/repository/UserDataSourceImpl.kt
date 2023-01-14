package live.kevalkanpariya.data.repository

import live.kevalkanpariya.domain.model.auth.User
import live.kevalkanpariya.domain.repository.UserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class UserDataSourceImpl(
    database: CoroutineDatabase
) : UserDataSource {

    private val users = database.getCollection<User>()

    override suspend fun getUserInfo(userID: String): User? {
        return users.findOne(filter = User::id eq userID)
    }

    override suspend fun saveUserInfo(user: User): Boolean {
        val existingUser = users.findOne(filter = User::id eq user.id)
        return if (existingUser == null) {
            users.insertOne(document = user).wasAcknowledged()
        } else {
            true
        }
    }

    override suspend fun deleteUser(userID: String): Boolean {
        return users.deleteOne(filter = User::id eq userID).wasAcknowledged()
    }

    override suspend fun updateUserInfo(
        userId: String,
        firstName: String,
        lastName: String
    ): Boolean {
        return users.updateOne(
            filter = User::id eq userId,
            update = setValue(
                property = User::name,
                value = "$firstName $lastName"
            )
        ).wasAcknowledged()
    }
}