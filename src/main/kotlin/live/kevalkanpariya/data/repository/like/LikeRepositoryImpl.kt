package live.kevalkanpariya.data.repository.like

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Comment
import live.kevalkanpariya.data.models.Course
import live.kevalkanpariya.data.models.Like
import live.kevalkanpariya.data.models.User
import live.kevalkanpariya.data.util.ParentType
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.inc
import org.litote.kmongo.setValue

class LikeRepositoryImpl(
    db: CoroutineDatabase
): LikeRepository {

    private val likes = db.getCollection<Like>()
    private val users = db.getCollection<User>()
    private val comments = db.getCollection<Comment>()
    private val courses = db.getCollection<Course>()
    override suspend fun likeParent(userId: String, parentId: String, parentType: Int,app: Application): Boolean {
        val doesUserExist = users.findOne(filter = User::id eq userId) != null
        app.log.info("use exists or not $doesUserExist")
        return if(doesUserExist) {
            when(parentType) {
                ParentType.Comment.type -> {
                    comments.updateOne(
                        filter = Comment::id eq parentId,
                        update = inc(Comment::likeCount, 1)
                    )
                }
            }
            likes.insertOne(Like(userId, parentId, parentType, System.currentTimeMillis()))
            true
        } else {
            false
        }
    }

    override suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean {
        val doesUserExist = users.findOne(User::id eq userId) != null
        return if(doesUserExist) {
            when(parentType) {
                ParentType.Comment.type -> {
                    comments.updateOne(
                        filter = Comment::id eq parentId,
                        update = inc(Comment::likeCount, -1)
                    )
                }
            }
            likes.deleteOne(
                and(
                    Like::userId eq userId,
                    Like::parentId eq parentId
                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun deleteLikesForParent(parentId: String) {
        likes.deleteMany(Like::parentId eq parentId)
    }

    override suspend fun getLikesForParent(parentId: String, page: Int, pageSize: Int): List<Like> {
        return likes
            .find(Like::parentId eq parentId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Like::timestamp)
            .toList()
    }
}