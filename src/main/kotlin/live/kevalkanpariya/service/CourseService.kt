package live.kevalkanpariya.service

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.*
import live.kevalkanpariya.data.repository.course.CourseRepository
import live.kevalkanpariya.data.requests.CourseRequest
import live.kevalkanpariya.data.requests.LessonRequest
import live.kevalkanpariya.util.Constants

class CourseService(
    private val courseRepository: CourseRepository
) {

    suspend fun searchCourses(
        query: String,
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.searchCourses(query, page, pageSize, app)
    }
    suspend fun getCourseById(courseId: String): Course? {
        return courseRepository.getCourseById(courseId)
    }


    suspend fun getCoursesForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getCoursesForProfile(userId, page, pageSize, app)
    }

    suspend fun getMostWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getMostWatchedCourses(page, pageSize, app)
    }
    suspend fun getPreviousWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>? {
        return courseRepository.getPreviousWatchedCourses(page, pageSize)
    }
    suspend fun getOthersWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>? {
        return courseRepository.getOthersWatchedCourses(page, pageSize)
    }
    suspend fun getCourseDetails(courseId: String, app: Application): Course? {
        return courseRepository.getCourseDetails(courseId, app)
    }

    private suspend fun getTeacherInfoForCourse(userId: String): Teacher? {
        return courseRepository.getTeacherInfoForCourse(userId)
    }

    suspend fun createCourse(courseIntroVideoUrl: String, imageUrl: String?, courseRequest: CourseRequest, userId: String): Boolean {
        return courseRepository.createCourse(
            Course(
                courseTitle = courseRequest.courseTitle,
                courseThumbnail = imageUrl?: Constants.DEFAULT_BANNER_IMAGE_PATH,
                description = courseRequest.description,
                courseTeacher = getTeacherInfoForCourse(userId),
                noOfLessons = 0,
                duration = courseRequest.duration,
                tag = courseRequest.tag,
                courseIntroVideoUrl = courseIntroVideoUrl
            )
        )
    }

    suspend fun updateCourseInfo(
        courseId: String,
        imageUrl: String?,
        courseRequest: CourseRequest,
    ): Boolean {
        return courseRepository.updateCourseInfo(courseId, imageUrl,courseRequest)
    }

    suspend fun deleteCourse(courseId: String): Boolean {
        return courseRepository.deleteCourse(courseId)
    }

    suspend fun enrollCourse(courseId: String, userId: String, app:Application): Boolean {
        return courseRepository.enrollCourse(courseId, userId, app)
    }

    suspend fun hasEnrolledCourse(courseId: String, userId: String): Boolean {
        return courseRepository.hasEnrolledCourse(courseId, userId)
    }

    suspend fun rateCourse(courseId: String, userId: String, ratingValue: Int, app: Application): Boolean {
        return courseRepository.rateCourse(courseId, userId, ratingValue, app)
    }



}