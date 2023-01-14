package live.kevalkanpariya.domain.model.api_response

@kotlinx.serialization.Serializable
data class ImageApiResponse(
    val success: Boolean,
    val moduleImage: String? = null,
    val message: String? = null
)
