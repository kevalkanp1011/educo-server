package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Comment(
    @BsonId
    val id: String = ObjectId().toString(),
    val userId: String,
    val courseId: String,
    val username: String,
    val profileImageUrl: String,
    val comment: String,
    val likeCount: Int,
    val timestamp: Long,
)