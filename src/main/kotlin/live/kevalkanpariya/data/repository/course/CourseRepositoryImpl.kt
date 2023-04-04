package live.kevalkanpariya.data.repository.course

import com.mongodb.client.model.Accumulators.avg
import io.ktor.server.application.*
import live.kevalkanpariya.data.models.*
import live.kevalkanpariya.data.requests.CourseRequest
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.aggregate
import org.litote.kmongo.coroutine.insertOne
import java.util.*

class CourseRepositoryImpl(db: CoroutineDatabase): CourseRepository {

    private val courses = db.getCollection<Course>()
    private val users = db.getCollection<User>()
    private val enrollments = db.getCollection<Enrollment>()
    private val ratings = db.getCollection<Rating>()
    private val categories = db.getCollection<Category>()
    private val bookmarks = db.getCollection<Bookmark>()
    override suspend fun searchCourses(
        query: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<CourseOverview>? {
        TODO("Not yet implemented")
    }


    override suspend fun getCourseById(id: String): Course? {
        return courses.findOne(filter = Course::courseId eq id)
    }

    override suspend fun getCoursesForProfile(userId: String, page: Int, pageSize: Int, app: Application): List<CourseOverview>? {
        val courseOverviews =  courses.find(filter = Course::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map { course ->
                app.log.info(course.courseTitle)
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.name,
                    courseThumbnailUrl = course.courseThumbnail,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag
                )

            }
        return courseOverviews
    }

    override suspend fun getMostWatchedCourses(page: Int, pageSize: Int, app: Application): List<CourseOverview> {

        val courseOverviews =  courses.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map { course ->
                app.log.info(course.courseTitle)
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.name,
                    courseThumbnailUrl = course.courseThumbnail,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag
                )

            }
        return courseOverviews
    }

    override suspend fun getPreviousWatchedCourses(page: Int, pageSize: Int): List<CourseOverview>? {
        return courses.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map {course ->
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.name,
                    courseThumbnailUrl = course.courseThumbnail,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag
                )
            }
    }

    override suspend fun getOthersWatchedCourses(page: Int, pageSize: Int): List<CourseOverview>? {
        return courses.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map {course ->
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.name,
                    courseThumbnailUrl = course.courseThumbnail,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag
                )
            }
    }

    override suspend fun getCourseDetails(courseId: String, app: Application): Course? {
        app.log.info(courseId)
        val course = courses.findOne(Course::courseId eq courseId)
        app.log.info(course?.courseTitle)
        return course
    }

    override suspend fun AddCourseToBookmark(courseId: String, userId: String): Boolean {
        return bookmarks.insertOne(
            Bookmark(
                userId = userId,
                courseId = courseId
            )
        ).wasAcknowledged()
    }

    override suspend fun removeCourseFromBookmark(courseId: String, userId: String): Boolean {
        val isDeleted = bookmarks.deleteOne(
            filter = and(
                Bookmark::courseId eq courseId,
                Bookmark::userId eq userId
            )
        ).wasAcknowledged()
        return isDeleted
    }

    override suspend fun createCourse(course: Course): Boolean {
        return courses.insertOne(course).wasAcknowledged()
    }

    override suspend fun updateCourseInfo(courseId: String, imageUrl:String?, courseRequest: CourseRequest): Boolean {
        val course = courses.findOne(Course::courseId eq courseId)?: return false
        return courses.updateOne(
            filter = Course::courseId eq courseId,
            update = combine(
                setValue(Course::courseTitle, courseRequest.courseTitle),
                setValue(Course::description, courseRequest.description),
                setValue(Course::courseThumbnail, imageUrl?: course.courseThumbnail),
                setValue(Course::tag, courseRequest.tag),
                setValue(Course::duration, courseRequest.duration)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteCourse(courseId: String): Boolean {
        return courses.deleteOne(
            filter = Course::courseId eq courseId
        ).wasAcknowledged()
    }

    override suspend fun getTeacherInfoForCourse(userId: String): Teacher? {
        val user = users.findOne(User::id eq userId)?: return null
        return Teacher(
            name = user.username,
            profileImageUrl = user.profileImageUrl
        )
    }

    override suspend fun enrollCourse(courseId: String, userId: String, app:Application): Boolean {
        val isEnrollmentNotExists = enrollments.findOne(
            and(
                Enrollment::courseId eq courseId,
                Enrollment::userId eq userId
        )) == null
        if(!isEnrollmentNotExists) {
            return false
        }

        enrollments.insertOne(
            Enrollment(
                userId,
                courseId,
                Date()
            )
        )
        return courses.updateOne(
            filter = Course::courseId eq courseId,
            update = inc(Course::noOfStudentEnrolled, 1)
        ).wasAcknowledged()

    }

    override suspend fun hasEnrolledCourse(courseId: String, userId: String): Boolean {
        return enrollments.findOne(
            and(
                Enrollment::courseId eq courseId,
                Enrollment::userId eq userId
            )
        ) != null
    }

    override suspend fun rateCourse(courseId: String, userId: String, ratingValue: Int, app:Application): Boolean {
        val isRatingNotExists = ratings.findOne(
            and(
                Rating::courseId eq courseId,
                Rating::userId eq userId
            )
        ) == null

        if (!isRatingNotExists ) {
            return false
        }
        val isRatedSuccessful =  ratings.insertOne(
            Rating(
                userId,
                courseId,
                ratingValue,
                Date()
            )
        ).wasAcknowledged()
        val avgRating = getAvgRating(courseId, app)
        app.log.info("$avgRating")
        return if (isRatedSuccessful)
            courses.updateOne(
                filter = Course::courseId eq courseId,
                update = combine(
                    setValue(Course::avgRating, avgRating),
                    inc(Course::noOfStudentRated, 1),
                )
            ).wasAcknowledged()
        else
            false

    }

    override suspend fun getAvgRating(courseId: String, app: Application): Double {
        val pipeline = group(courseId, avg("avg_rating", "\$ratingValue"))
        val result = ratings.aggregate<AvgRating>(pipeline)
        app.log.info("${result.first()?.avg_rating}")
        return result.first()?.avg_rating ?: 0.0
    }


}