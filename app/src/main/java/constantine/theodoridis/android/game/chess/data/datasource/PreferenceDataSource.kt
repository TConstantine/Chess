package constantine.theodoridis.android.game.chess.data.datasource

interface PreferenceDataSource {
    fun contains(key: String): Boolean
    fun getInt(key: String, defaultValue: Int): Int
}
