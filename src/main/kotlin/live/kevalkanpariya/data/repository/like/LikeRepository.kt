package live.kevalkanpariya.data.repository.like

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Like
import live.kevalkanpariya.util.Constants

interface LikeRepository {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int, app: Application): Boolean

    suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun deleteLikesForParent(parentId: String)

    suspend fun getLikesForParent(
        parentId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<Like>
}