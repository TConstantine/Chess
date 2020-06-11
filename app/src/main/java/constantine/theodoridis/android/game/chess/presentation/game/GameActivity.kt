package constantine.theodoridis.android.game.chess.presentation.game

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.presentation.settings.SettingsActivity

class GameActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var chessBoardView: ChessBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        chessBoardView = findViewById(R.id.chess_board_view)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val boardSize = sharedPreferences!!.getInt(
            getString(R.string.board_size_preference_key),
            resources.getInteger(R.integer.default_board_size)
        )
        chessBoardView.setSize(boardSize)
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // TODO
        if (key == getString(R.string.board_size_preference_key)) {
            val boardSize = sharedPreferences!!.getInt(
                getString(R.string.board_size_preference_key),
                resources.getInteger(R.integer.default_board_size)
            )
            chessBoardView.setSize(boardSize)
            chessBoardView.invalidate()
        }
    }
}