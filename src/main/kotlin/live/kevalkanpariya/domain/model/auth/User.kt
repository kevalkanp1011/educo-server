package live.kevalkanpariya.domain.model.auth

@kotlinx.serialization.Serializable
data class User(
    val id:String,
    val name: String,
    val emailAddress: String,
    val profilePhoto: String,
)
