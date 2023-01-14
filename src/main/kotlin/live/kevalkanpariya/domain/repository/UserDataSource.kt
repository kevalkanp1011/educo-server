package live.kevalkanpariya.domain.repository

import live.kevalkanpariya.domain.model.auth.User

interface UserDataSource {
    suspend fun getUserInfo(userID: String): User?
    suspend fun saveUserInfo(user: User): Boolean
    suspend fun deleteUser(userID: String): Boolean
    suspend fun updateUserInfo(
        userId: String,
        firstName: String,
        lastName: String
    ): Boolean

}