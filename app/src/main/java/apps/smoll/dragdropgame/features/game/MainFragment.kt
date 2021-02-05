package apps.smoll.dragdropgame.features.game

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.utils.*
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber


class MainFragment : Fragment(R.layout.fragment_main) {

    val gameViewModel: GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObservingLiveData()
        initListeners()
        with(containerView) {
            post {
                gameViewModel.startGame(width, height)
            }
        }
    }

    private fun startObservingLiveData() {
        gameViewModel.scoreText.observe(
            viewLifecycleOwner,
            { scoreTextView.text = it }
        )

        gameViewModel.timerText.observe(
            viewLifecycleOwner,
            { timeLeftTextView.text = it }
        )

        gameViewModel.levelText.observe(
            viewLifecycleOwner,
            { levelTextView.text = it }
        )

        gameViewModel.shapeToMatch.observe(
            viewLifecycleOwner,
            { updateShapeToMatch(it) }
        )

        gameViewModel.screenShapes.observe(
            viewLifecycleOwner,
            { updateShapesOnScreen(it) }
        )

        gameViewModel.userWonEvent.observe(
            viewLifecycleOwner,
            {
                it.getContentIfNotHandled()?.let {
                    onUserWon()
                }
            }
        )

        gameViewModel.userLostEvent.observe(
            viewLifecycleOwner,
            {
                it.getContentIfNotHandled()?.let {
                    onUserLost()
                }
            }
        )
    }

    private fun onUserWon() {
        mainMenuButton.visible()
        nextLevelButton.visible()
    }

    private fun onUserLost() {
        mainMenuButton.visible()
        retryButton.visible()
    }

    private fun hideAllButtons() {
        mainMenuButton.gone()
        nextLevelButton.gone()
        retryButton.gone()
    }

    private fun updateShapeToMatch(shape: Shape?) {
        if (shape != null) {
            dragImageView.apply {
                setShape(requireContext(), shape)
            }
        } else {
            dragImageView.gone()
        }
    }

    private fun initListeners() {
        nextLevelButton.setOnClickListener {
            hideAllButtons()


            gameViewModel.startGame(containerView!!.width, containerView!!.height)

//            gameViewModel.startGame(screenWidthAndHeight)
        }
        retryButton.setOnClickListener {
            hideAllButtons()
            gameViewModel.restartLevel(screenWidthAndHeight)
        }

        dragImageView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {

                ACTION_DOWN -> {
                    val item = ClipData.Item(view.tag as? CharSequence)
                    val dragData = ClipData(
                        view.tag as? CharSequence,
                        arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                        item
                    )

                    val myShadow = MyDragShadowBuilder(dragImageView)
                    val dragShadow = View.DragShadowBuilder(dragImageView)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view.startDragAndDrop(dragData, dragShadow, null, 0);
                    } else {
                        view.startDrag(
                            dragData,
                            myShadow,
                            null,
                            0
                        )
                    }
                    view.performClick()
                }
            }
            true
        }

        val dragListen = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    dragImageView.invisible()
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DROP -> {


                    gameViewModel.handleMatchingShapeDrop(Pair(event.x.toInt(), event.y.toInt()))
                    v.invalidate()
                    true
                }
                else -> {
                    // An unknown action type was received.
                    false
                }
            }
        }
        containerView.setOnDragListener(dragListen)

    }

    private fun updateShapesOnScreen(shapes: List<Shape>) {
        clearPreviouslyConstructedShapes()

        for (shape in shapes) {
            ImageView(requireContext()).apply {
                setShape(requireContext(), shape)
                id = View.generateViewId()
                gameViewModel.addedViewIds.add(id)
                requestLayout()
                containerView.addView(this)
            }
        }

        containerView!!.post {
            Timber.d("containerView height after shapes laid out = ${containerView!!.height}")
        }
    }

    private fun clearPreviouslyConstructedShapes() {
        gameViewModel.addedViewIds.apply {
            forEach {
                containerView.apply {
                    Timber.d("Removing view with id: $it")
                    removeView(findViewById(it))
                }
            }
        }
    }

    private val screenWidthAndHeight: Pair<Int, Int>
        get() {
            val displayMetrics = DisplayMetrics()
            requireActivity().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    display?.getRealMetrics(displayMetrics)
                } else {
                    windowManager!!.defaultDisplay.getMetrics(displayMetrics)
                }
            }

            displayMetrics.apply {
                return Pair(widthPixels, heightPixels)
            }
        }
}



