package live.kevalkanpariya.util.rating

import live.kevalkanpariya.domain.model.db.Enrollment
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class EnrollmentImpl(db: CoroutineDatabase) {
    private val enrollments = db.getCollection<Enrollment>()

    suspend fun totalNoOfStudentEnrolled(): Long {
        return enrollments.countDocuments(Enrollment::isEnrolled eq true)
    }
}