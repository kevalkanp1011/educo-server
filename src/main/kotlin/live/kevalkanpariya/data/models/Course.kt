package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import live.kevalkanpariya.util.DurationSerializer
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlin.time.Duration

@OptIn(kotlin.time.ExperimentalTime::class)
@Serializable
data class Course(
    @BsonId
    val courseId: String = ObjectId().toString(),
    val userId: String = "",
    val courseTitle: String,
    val courseThumbnail: String,
    val courseIntroVideoUrl: String,
    val description: String,
    val moreDetails: String? = null,
    val courseTeacher : Teacher? = null,
    val noOfStudentEnrolled: Int = 0,
    val noOfLessons: Int,
    val noOfStudentRated: Int = 0,
    val avgRating: Double = 0.0,
    val tag: String? = null,
    val commentCount: Int = 0,
    @Serializable(with = DurationSerializer::class)
    val duration: Duration? = null,

)