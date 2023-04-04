package live.kevalkanpariya.data.repository.course

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.*
import live.kevalkanpariya.data.requests.CourseRequest
import live.kevalkanpariya.data.requests.LessonRequest
import live.kevalkanpariya.service.CourseService
import live.kevalkanpariya.util.Constants

interface CourseRepository {

    suspend fun searchCourses(
        query: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?
    suspend fun getCourseById(id: String): Course?

    suspend fun getCoursesForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?



    suspend fun getMostWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?
    suspend fun getPreviousWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>?
    suspend fun getOthersWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>?
    suspend fun getCourseDetails(courseId: String, app: Application): Course?


    suspend fun AddCourseToBookmark(courseId: String, userId: String): Boolean
    suspend fun removeCourseFromBookmark(courseId: String, userId: String): Boolean

    suspend fun getTeacherInfoForCourse(userId: String): Teacher?
    suspend fun enrollCourse(courseId: String, userId: String, app:Application): Boolean
    suspend fun hasEnrolledCourse(courseId: String, userId: String): Boolean
    suspend fun rateCourse(courseId: String, userId: String, ratingValue: Int, app: Application): Boolean
    suspend fun getAvgRating(courseId: String, app: Application): Double

    /*Teacher Access*/
    suspend fun createCourse(course: Course): Boolean
    suspend fun updateCourseInfo(courseId: String, imageUrl:String?, courseRequest: CourseRequest): Boolean
    suspend fun deleteCourse(courseId: String): Boolean


}