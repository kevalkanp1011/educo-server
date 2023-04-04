package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Project(
    @BsonId
    val projectId: String = ObjectId().toString(),
    val courseId: String,
    val projectName: String,
    val desc: String,
    val imageUrls: List<String>
)
