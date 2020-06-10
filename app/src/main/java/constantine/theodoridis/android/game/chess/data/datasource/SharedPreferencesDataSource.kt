package constantine.theodoridis.android.game.chess.data.datasource

import android.content.SharedPreferences

class SharedPreferencesDataSource(
    private val sharedPreferences: SharedPreferences
) : PreferenceDataSource {
    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
}
