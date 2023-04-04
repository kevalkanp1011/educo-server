package live.kevalkanpariya.data.repository.lesson

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Lesson
import live.kevalkanpariya.data.requests.LessonRequest

interface LessonRepository {

    suspend fun createLesson(lesson: Lesson, app: Application): Boolean

    suspend fun updateLesson(
        lessonId: String,
        lessonThumbnailUrl: String?,
        videoUrl: String?,
        updateLessonRequest: LessonRequest,
        app: Application
    ): Boolean

    suspend fun deleteLesson(lessonId: String): Boolean

    suspend fun deleteLessonsFromCourse(courseId: String): Boolean

    suspend fun getLessonsForCourse(courseId: String): List<Lesson>?

    suspend fun getLesson(lessonId: String): Lesson?

}