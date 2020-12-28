package apps.smoll.dragdropgame.features.game

import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import apps.smoll.dragdropgame.utils.MyDragShadowBuilder
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.ShapeType
import apps.smoll.dragdropgame.utils.invisible
import apps.smoll.dragdropgame.utils.setImage
import apps.smoll.dragdropgame.utils.visible
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {

    val addedViewIds = mutableSetOf<Int>()
    val shapesOnScreen = mutableSetOf<Shape>()
    var score = 0
    val matchingShape = Shape(Pair(500f, 500f), ShapeType.SQUARE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startGame()
        initListeners()
    }

    private fun initListeners() {
        restartGameButton.setOnClickListener { restartGame() }

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

                    val coordinates = Pair(event.x, event.y)
                    handleDrop(coordinates)
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

    private fun handleDrop(coordinates: Pair<Float, Float>) {
        if (isTargetGetHit(coordinates)) {
            scoreTextView.text = "Your score = ${++score}"
        }
        moveMatchingShapeToInitialPos()
    }

    fun moveMatchingShapeToInitialPos() {
        matchingShape.coordinates.apply {
            dragImageView.x = first
            dragImageView.y = second
        }
        dragImageView.visible()
    }

    private fun isTargetGetHit(targetCoordinates: Pair<Float, Float>): Boolean {
        for (shapeOnScreen in shapesOnScreen) {
            shapeOnScreen.apply {
                val shapeOnScreenXCenter = this.coordinates.first + shapeWidth / 2
                val shapeOnScreenYCenter = this.coordinates.second + shapeHeight / 2
                val permissibleXFaultRange =
                    shapeOnScreenXCenter - permissibleHitFaultInPixels..shapeOnScreenXCenter + permissibleHitFaultInPixels
                val permissibleYFaultRange =
                    shapeOnScreenYCenter - permissibleHitFaultInPixels..shapeOnScreenYCenter + permissibleHitFaultInPixels

                val isXHit =
                    targetCoordinates.first in permissibleXFaultRange
                val isYHit =
                    targetCoordinates.second in permissibleYFaultRange

                val shapeMatch = matchingShape.shapeType == shapeType
                if (isXHit && isYHit && shapeMatch) return true
            }
        }
        return false
    }

    private fun startGame() {
        drawMatchingShape()
        generateShapesOnScreen()
    }

    private fun drawMatchingShape() {
        val imageShapeArray = arrayOf(
            R.drawable.ic_square,
            R.drawable.ic_hexagonal,
            R.drawable.ic_star,
            R.drawable.ic_circle
        )

        val shapeTypeInt = Random().nextInt(imageShapeArray.size)

        matchingShape.shapeType = when (shapeTypeInt) {
            0 -> ShapeType.SQUARE
            1 -> ShapeType.HEXAGON
            2 -> ShapeType.STAR
            3 -> ShapeType.CIRCLE
            else -> ShapeType.SQUARE
        }

        dragImageView.setImage(
            requireContext(),
            imageShapeArray[shapeTypeInt]
        )
    }


    private fun restartGame() {
        score = 0
        scoreTextView.text = "Your score = $score"
        generateShapesOnScreen()
    }

    private fun generateShapesOnScreen() {
        fun clearPreviousViews() {
            if (addedViewIds.isNotEmpty()) {
                for (viewId in addedViewIds) {
                    containerView.removeView(containerView.findViewById(viewId))
                }
            }
            addedViewIds.clear()
            shapesOnScreen.clear()
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

        val imageShapeArray = arrayOf(
            R.drawable.ic_square,
            R.drawable.ic_hexagonal,
            R.drawable.ic_star,
            R.drawable.ic_circle
        )

        val colors: IntArray = resources.getIntArray(R.array.shape_colors)

        imageShapeArray.shuffle()
        colors.shuffle()

        var startX = 50f

        val displayMetrics = DisplayMetrics()

        requireActivity().apply {
            windowManager!!.defaultDisplay.getMetrics(displayMetrics)

        }
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        val xSpacing = screenWidth / imageShapeArray.size

        if (colors.size < imageShapeArray.size) {
            return
        }

        for (index in imageShapeArray.indices) {
            val imageView = ImageView(requireContext())
            imageView.apply {
                setImage(requireContext(), imageShapeArray[index])

                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colors[index]));

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

                shapesOnScreen.add(
                    Shape(
                        Pair(xCoord, yCoord), when (index) {
                            0 -> ShapeType.SQUARE
                            1 -> ShapeType.HEXAGON
                            2 -> ShapeType.STAR
                            3 -> ShapeType.CIRCLE
                            else -> ShapeType.SQUARE
                        }
                    )
                )

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



