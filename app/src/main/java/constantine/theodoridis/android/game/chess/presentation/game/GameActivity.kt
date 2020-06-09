package constantine.theodoridis.android.game.chess.presentation.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.presentation.mainmenu.MainMenuActivity

class GameActivity : AppCompatActivity() {
    private lateinit var chessBoardView: ChessBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        chessBoardView = findViewById(R.id.chess_board_view)
        val boardSize = intent.getStringExtra(MainMenuActivity.BOARD_SIZE_EXTRA)
//        val moves = intent.getStringExtra(MainMenuActivity.MOVES_EXTRA)
        chessBoardView.setSize(Integer.parseInt(boardSize!!))
    }
}