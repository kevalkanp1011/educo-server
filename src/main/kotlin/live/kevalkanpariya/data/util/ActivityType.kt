package live.kevalkanpariya.data.util

sealed class ActivityType(val type: Int) {
    object LikedCourse : ActivityType(0)
    object LikedComment : ActivityType(1)
    object CommentedOnCourse : ActivityType(2)
    object FollowedUser : ActivityType(3)
}