package live.kevalkanpariya.domain.model.course

import kotlinx.serialization.Serializable
import live.kevalkanpariya.domain.model.teacher.Teacher
import live.kevalkanpariya.domain.model.lesson.Lesson
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.sql.Timestamp

@Serializable
data class Course(
    @BsonId
    var id: String = ObjectId().toString(),
    val courseTitle: String,
    var imageUrl: String,
    val description: String,
    val noOfStudentEnrolled: Int,
    val course_teacher : Teacher,
    var noOfLessons: Int,
    val noOfStudentRated: Int,
    val rating: Double,
    val tag: String? = null,
    val lessons: List<Lesson>,
    val time: String,
)
