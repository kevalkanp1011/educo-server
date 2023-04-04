package live.kevalkanpariya.data.requests

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RatingRequest(
    @SerializedName("ratingValue")
    val ratingValue: Int
)
