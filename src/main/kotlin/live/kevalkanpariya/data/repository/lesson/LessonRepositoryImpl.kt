package live.kevalkanpariya.data.repository.lesson

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Course
import live.kevalkanpariya.data.models.Lesson
import live.kevalkanpariya.data.requests.LessonRequest
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class LessonRepositoryImpl(
    db: CoroutineDatabase
): LessonRepository {

    private val courses = db.getCollection<Course>()
    private val lessons = db.getCollection<Lesson>()

    override suspend fun createLesson(lesson: Lesson, app: Application): Boolean {
        return lessons.insertOne(lesson).wasAcknowledged()
    }

    override suspend fun updateLesson(
        lessonId: String,
        lessonThumbnailUrl: String?,
        videoUrl: String?,
        updateLessonRequest: LessonRequest,
        app: Application
    ): Boolean {
        return courses.updateOne(
            filter = Lesson::lessonId eq lessonId,
            update = combine(
                Updates.set(Lesson::lessonNo.toString(), updateLessonRequest.lessonNo),
                Updates.set(Lesson::name.toString(), updateLessonRequest.name),
                Updates.set(Lesson::thumbnail.toString(), lessonThumbnailUrl),
                Updates.set(Lesson::desc.toString(), updateLessonRequest.desc),
                Updates.set(Lesson::lessonVideoUrl.toString(), videoUrl)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteLesson(lessonId: String): Boolean {
        return lessons.deleteOne(Lesson::lessonId eq lessonId).wasAcknowledged()
    }

    override suspend fun deleteLessonsFromCourse(courseId: String): Boolean {
        return lessons.deleteMany(Lesson::courseId eq courseId).wasAcknowledged()
    }

    override suspend fun getLessonsForCourse(courseId: String): List<Lesson>? {
        return lessons.find(Lesson::courseId eq courseId).toList()
    }


    override suspend fun getLesson(lessonId: String): Lesson? {
        return lessons.findOne(Lesson::lessonId eq lessonId)
    }
}