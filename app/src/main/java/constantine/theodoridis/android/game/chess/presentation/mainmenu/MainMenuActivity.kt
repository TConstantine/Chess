package constantine.theodoridis.android.game.chess.presentation.mainmenu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.domain.usecase.ValidateMenuInputUseCase
import constantine.theodoridis.android.game.chess.presentation.mainmenu.model.MainMenuViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivity : AppCompatActivity() {
    private lateinit var boardSizeEditText: EditText
    private lateinit var boardSizeErrorTextView: TextView
    private lateinit var movesEditText: EditText
    private lateinit var movesErrorTextView: TextView
    private lateinit var startButton: Button

    private lateinit var presenter: MainMenuPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardSizeEditText = findViewById<View>(R.id.board_size) as EditText
        boardSizeErrorTextView = findViewById<View>(R.id.board_size_error) as TextView
        movesEditText = findViewById<View>(R.id.moves) as EditText
        movesErrorTextView = findViewById<View>(R.id.moves_error) as TextView
        startButton = findViewById<View>(R.id.start) as Button
        val useCase = ValidateMenuInputUseCase()
        presenter = ViewModelProvider(this, object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass == MainMenuPresenter::class.java) {
                    return MainMenuPresenter(useCase, AndroidSchedulers.mainThread()) as T
                }
                throw IllegalArgumentException("Factory cannot create ViewModel of type ${modelClass.simpleName}")
            }
        }).get(MainMenuPresenter::class.java)
        presenter.viewModelObservable().observe(this, Observer { viewModel ->
            handleOnClick(viewModel)
        })
        startButton.setOnClickListener {
            presenter.onStart(boardSizeEditText.text.toString(), movesEditText.text.toString())
        }
    }

    private fun handleOnClick(viewModel: MainMenuViewModel) {
        boardSizeErrorTextView.visibility = View.GONE
        movesErrorTextView.visibility = View.GONE
        if (!viewModel.hasBoardSizeError && !viewModel.hasMovesError) {
            Log.d("MainMenuActivity", "Success")
        } else {
            if (viewModel.hasBoardSizeError) {
                boardSizeErrorTextView.text = viewModel.boardSizeErrorMessage
                boardSizeErrorTextView.visibility = View.VISIBLE
            }
            if (viewModel.hasMovesError) {
                movesErrorTextView.text = viewModel.movesErrorMessage
                movesErrorTextView.visibility = View.VISIBLE
            }
        }

    }
}