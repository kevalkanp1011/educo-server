package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Resource(
    @BsonId
    val resourceId: String = ObjectId().toString(),
    val courseId: String,
    val resourceUrl: String,
    val resourceName: String,
    val resourceSize: Double,
    val fileType: String
)
