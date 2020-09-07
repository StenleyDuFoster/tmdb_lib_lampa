package lampa.test.tmdblib.model.viewmodel.repository.data

data class SessionTmdbData(

    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)