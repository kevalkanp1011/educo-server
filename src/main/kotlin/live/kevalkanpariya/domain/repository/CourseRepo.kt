package live.kevalkanpariya.domain.repository

import live.kevalkanpariya.domain.model.course.Course

interface CourseRepo {
    suspend fun getCourseById(id: String): Course?
    suspend fun createCourse(course: Course): Boolean
    suspend fun updateCourseById(course: Course): Boolean
    suspend fun deleteCourseById(id: String): Boolean
}