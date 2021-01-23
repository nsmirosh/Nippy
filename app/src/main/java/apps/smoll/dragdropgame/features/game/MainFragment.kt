package apps.smoll.dragdropgame.features.game

import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.utils.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(R.layout.fragment_main) {

    val addedViewIds = mutableSetOf<Int>()

    val gameViewModel: GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel.startGame(screenWidthAndHeight)
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

        gameViewModel.screenShapesLiveData.observe(
            viewLifecycleOwner,
            { updateShapesOnScreen(it) }
        )
        gameViewModel.timeLeftLiveData.observe(
            viewLifecycleOwner,
            { updateTimerText(it) }
        )
    }

    private fun updateTimerText(secondsLeft: String) {
        timeLeftTextView.text = secondsLeft
    }

    private fun updateScoreText(score: String) {
        scoreTextView.text = score
    }

    private fun updateShapeToMatch(shape: Shape) {
        shape.shapeCenter.apply {
            dragImageView.x = first.toFloat()
            dragImageView.y = second.toFloat()
        }
        dragImageView.visible()
        dragImageView.setImage(
            requireContext(),
            shape.typeResource
        )
    }

    private fun initListeners() {
        restartGameButton.setOnClickListener { gameViewModel.restartGame(screenWidthAndHeight) }

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
                    gameViewModel.handleDrop(Pair(event.x.toInt(), event.y.toInt()))
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
        clearScreenShapes()

        for (shape in shapes) {
            val imageView = ImageView(requireContext())
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            imageView.apply {
                setImage(requireContext(), shape.typeResource)


                ImageViewCompat.setImageTintList(
                    this,
                    ColorStateList.valueOf(getColor(requireContext(), shape.colorResource))
                )
                id = View.generateViewId();
                addedViewIds.add(id)
                layoutParams.height = shape.shapeSize;
                layoutParams.width = shape.shapeSize;
                requestLayout();

                shape.shapeCenter.apply {
                    x = first.toFloat()
                    y = second.toFloat()
                }
            }
            containerView.addView(imageView)
            setViewConstraints(imageView)
        }
    }

    private fun clearScreenShapes() {
        addedViewIds.apply {
            if (isNotEmpty()) {
                for (viewId in this) {
                    containerView.apply {
                        removeView(findViewById(viewId))
                    }
                }
            }
            clear()
        }
    }


    private fun setViewConstraints(view: View) {
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



