package lampa.test.tmdblib.model.repository.data

data class Session(

    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)