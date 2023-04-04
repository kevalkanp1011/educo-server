package live.kevalkanpariya.service

import live.kevalkanpariya.data.models.Activity
import live.kevalkanpariya.data.repository.activity.ActivityRepository
import live.kevalkanpariya.data.repository.comment.CommentRepository
import live.kevalkanpariya.data.repository.course.CourseRepository
import live.kevalkanpariya.data.util.ActivityType
import live.kevalkanpariya.data.util.ParentType

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val courseRepository: CourseRepository,
    private val commentRepository: CommentRepository
    ) {

    suspend fun createActivity(activity: Activity) {
        activityRepository.createActivity(activity)
    }

    suspend fun addCommentActivity(
        byUserId: String,
        courseId: String
    ): Boolean {
        val userIdOfCourse = courseRepository.getCourseById(courseId)?.userId ?: return false
        if(byUserId == userIdOfCourse) {
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfCourse,
                type = ActivityType.CommentedOnCourse.type,
                parentId = courseId
            )
        )
        return true
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ): Boolean {
        val toUserId = when (parentType) {
            is ParentType.Course -> {
                courseRepository.getCourseById(parentId)?.userId
            }
            is ParentType.Comment -> {
                commentRepository.getComment(parentId)?.userId
            }
            is ParentType.None -> return false
        } ?: return false
        if(byUserId == toUserId) {
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = toUserId,
                type = when (parentType) {
                    is ParentType.Course -> ActivityType.LikedCourse.type
                    is ParentType.Comment -> ActivityType.LikedComment.type
                    else -> ActivityType.LikedCourse.type
                },
                parentId = parentId
            )
        )
        return true
    }

    suspend fun deleteActivity(activityId: String): Boolean {
        return activityRepository.deleteActivity(activityId)
    }
}