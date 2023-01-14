package live.kevalkanpariya.data.repository


import live.kevalkanpariya.domain.model.course.Course
import live.kevalkanpariya.domain.repository.CourseRepo
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CourseRepoImpl(
    coro_database: CoroutineDatabase
): CourseRepo {

    private val courses = coro_database.getCollection<Course>()


    override suspend fun getCourseById(id: String): Course? {
        return courses.findOne(filter = Course::id eq id)
    }

    override suspend fun createCourse(course: Course): Boolean {
        course.id = ObjectId().toString()
        return courses.insertOne(course).wasAcknowledged()
    }

    override suspend fun updateCourseById(
        course: Course
    ): Boolean {
        val isCourseExist = courses.findOneById(course.id) != null
        return if (isCourseExist) {
            courses.updateOneById(course.id, course).wasAcknowledged()
        } else {
            false
        }
    }

    override suspend fun deleteCourseById(id: String): Boolean {
        val isCourseExist = courses.findOneById(id) != null
        return if (isCourseExist) {
            courses.deleteOneById(id).wasAcknowledged()
        } else {
            false
        }
    }

    /*private fun findHeroes(query: String?): List<Course> {
        val founded = mutableListOf<Course>()
        val ccc = courses.aggregate<Search>(match(filter = Course::courseTitle contains query))
        return if (!query.isNullOrEmpty()) {
            courses.forEach { course ->
                if (course.name.lowercase().contains(query.lowercase())) {
                    founded.add(course)
                }
            }
            founded
        } else {
            emptyList()
        }
    }*/



}