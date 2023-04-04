package live.kevalkanpariya.data.repository.feedback

import live.kevalkanpariya.data.models.FeedBack
import live.kevalkanpariya.data.models.User
import live.kevalkanpariya.data.requests.FeedbackRequest
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class FeedbackRepositoryImpl(
    db: CoroutineDatabase
): FeedbackRepository {

    private val feedbacks = db.getCollection<FeedBack>()
    private val users = db.getCollection<User>()

    override suspend fun getFeedBack(feedbackId: String): FeedBack? {
        return feedbacks.findOne(FeedBack::feedbackId eq feedbackId)
    }

    override suspend fun getAllFeedBacks(courseId: String): List<FeedBack> {
        return feedbacks.find(FeedBack::courseId eq courseId).toList()
    }

    override suspend fun createFeedback(userId: String, courseId: String, request: FeedbackRequest): Boolean {
        val user = users.findOne(User::id eq userId)?: return false
        return feedbacks.insertOne(
            FeedBack(
                userId = userId,
                courseId = courseId,
                feedback = request.feedback,
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                timestamp = request.timestamp
            )
        ).wasAcknowledged()
    }

    override suspend fun updateFeedback(feedbackId: String, feedback: FeedbackRequest): Boolean {
        return feedbacks.updateOne(
            filter = FeedBack::feedbackId eq feedbackId,
            update = combine(
                setValue(FeedBack::feedback, feedback.feedback),
                setValue(FeedBack::timestamp, feedback.timestamp)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteFeedback(feedbackId: String): Boolean {
        return feedbacks.deleteOne(FeedBack::feedbackId eq feedbackId).wasAcknowledged()
    }

    override suspend fun deleteAllFeedback(courseId: String): Boolean {
        return feedbacks.deleteMany(FeedBack::courseId eq courseId).wasAcknowledged()
    }

}