package apps.smoll.dragdropgame.features.game

import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.utils.MyDragShadowBuilder
import apps.smoll.dragdropgame.utils.invisible
import apps.smoll.dragdropgame.utils.setImage
import apps.smoll.dragdropgame.utils.visible
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {

    val addedViewIds = mutableSetOf<Int>()

    val gameViewModel: GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel.startGame()
        startObservingLiveData()
        initListeners()
    }

    private fun startObservingLiveData() {
        gameViewModel.scoreLiveData.observe(
            viewLifecycleOwner,
            { updateScoreText(it) }
        )
        gameViewModel.shapeToMatchLiveData.observe(
            viewLifecycleOwner,
            { updateShapeToMatch(it) }
        )
    }

    private fun updateShapeToMatch(shape: Shape) {
        shape.coordinates.apply {
            dragImageView.x = first
            dragImageView.y = second
        }
        dragImageView.visible()
        dragImageView.setImage(
            requireContext(),
            shape.typeResource
        )
    }

    private fun updateScoreText(score: Int) {
        scoreTextView.text = getString(R.string.score, score)
    }

    private fun initListeners() {
        restartGameButton.setOnClickListener { gameViewModel.restartGame() }

        dragImageView.setOnLongClickListener { v: View ->
            val item = ClipData.Item(v.tag as? CharSequence)
            val dragData = ClipData(
                v.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val myShadow = MyDragShadowBuilder(dragImageView)
            val dragShadow = View.DragShadowBuilder(dragImageView)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(dragData, dragShadow, null, 0);
            } else {
                v.startDrag(
                    dragData,
                    myShadow,
                    null,
                    0
                )
            }
        }

        val dragListen = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    dragImageView.invisible()
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DROP -> {
                    gameViewModel.handleDrop(Pair(event.x, event.y))
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

    private fun generateShapesOnScreen(shapes: List<Shape>) {
        fun clearPreviousViews() {
            if (addedViewIds.isNotEmpty()) {
                for (viewId in addedViewIds) {
                    containerView.removeView(containerView.findViewById(viewId))
                }
            }
            addedViewIds.clear()
        }

        fun setViewConstraints(view: View) {
            val constraintSet = ConstraintSet()
            constraintSet.apply {
                clone(containerView)
                connect(
                    view.getId(),
                    ConstraintSet.TOP,
                    containerView.getId(),
                    ConstraintSet.TOP
                )
                applyTo(containerView)
            }
        }

        clearPreviousViews()

        var startX = 50f

        val displayMetrics = DisplayMetrics()

        requireActivity().apply {
            windowManager!!.defaultDisplay.getMetrics(displayMetrics)

        }
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val xSpacing = screenWidth / shapes.size


    }


    private fun buildShapeOnScreen() {


        //TODO generate shapes on screens so that they don't collide with each other
        for (shape in shapes) {
            val imageView = ImageView(requireContext())
            imageView.apply {
                setImage(requireContext(), shape.type)

                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(shape.color));

                id = View.generateViewId();
                addedViewIds.add(id)
                containerView.addView(this)

                layoutParams.height = shapeHeight;
                layoutParams.width = shapeWidth;
                this.requestLayout();

                val xCoord = startX
                val yCoord = Random().nextInt((screenHeight * 0.6).toInt()).toFloat()

                x = xCoord
                y = yCoord

                startX += xSpacing
            }
            setViewConstraints(imageView)
        }

    }

    companion object {
        const val shapeHeight = 150
        const val shapeWidth = 150
        const val permissibleHitFaultInPixels = 50
    }
}



