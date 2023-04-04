package live.kevalkanpariya.data.repository.feedback

import live.kevalkanpariya.data.models.FeedBack
import live.kevalkanpariya.data.requests.FeedbackRequest

interface FeedbackRepository {
    suspend fun getFeedBack(feedbackId: String): FeedBack?
    suspend fun getAllFeedBacks(courseId: String): List<FeedBack>
    suspend fun createFeedback(userId: String, courseId: String, request: FeedbackRequest): Boolean
    suspend fun updateFeedback(feedbackId: String, feedback: FeedbackRequest): Boolean
    suspend fun deleteFeedback(feedbackId: String): Boolean
    suspend fun deleteAllFeedback(courseId: String): Boolean
}