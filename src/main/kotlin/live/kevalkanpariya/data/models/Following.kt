package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Following(
    @BsonId
    val id: String = ObjectId().toString(),
    val followingUserId: String,
    val followedUserId: String,
)
