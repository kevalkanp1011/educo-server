package live.kevalkanpariya.data.repository.activity

import live.kevalkanpariya.data.models.Activity
import live.kevalkanpariya.data.responses.ActivityResponse
import live.kevalkanpariya.util.Constants

interface ActivityRepository {
    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<ActivityResponse>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean
}