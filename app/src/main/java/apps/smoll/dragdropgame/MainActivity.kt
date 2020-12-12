package apps.smoll.dragdropgame

import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.ImageViewCompat
import apps.smoll.dragdropgame.utils.settleInPosition
import kotlinx.android.synthetic.main.activity_main.*
import apps.smoll.dragdropgame.ShapeType.*
import apps.smoll.dragdropgame.utils.setImage
import java.util.*

const val IMAGEVIEW_TAG = "icon bitmap"

class MainActivity : AppCompatActivity() {

    val addedViewIds = mutableSetOf<Int>()
    val shapesOnScreen = mutableSetOf<Shape>()
    var score = 0
    var shapeTypeToMatch = SQUARE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                    // Determines if this View can accept the dragged data
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DROP -> {
                    handleDrop(event)
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

    private fun handleDrop(event: DragEvent) {
        dragImageView.settleInPosition(event)
        if (isTargetGetHit(event)) {
            scoreTextView.text = "Your score = ${++score}"
        }
    }

    private fun isTargetGetHit(event: DragEvent): Boolean {
        var isXHit = false
        var isYHit = false
        for (coordinate in shapesOnScreen) {
            coordinate.apply {
                val viewXCenter = this.coordinates.first + shapeWidth / 2
                val viewYCenter = this.coordinates.second + shapeHeight / 2

                isXHit =
                    event.x in viewXCenter - hitFaultInPixels..viewXCenter + hitFaultInPixels
                isYHit =
                    event.y in viewYCenter - hitFaultInPixels..viewYCenter + hitFaultInPixels
                if (isXHit && isYHit) {
                    return true
                }
            }
        }
        return isXHit && isYHit
    }

    private fun startGame() {

        val imageShapeArray = arrayOf(
            R.drawable.ic_square,
            R.drawable.ic_hexagonal,
            R.drawable.ic_star,
            R.drawable.ic_circle
        )

        val randomPosition = Random().nextInt(4)
        shapeTypeToMatch = when (randomPosition) {
            0 -> SQUARE
            1 -> HEXAGON
            2 -> STAR
            3 -> CIRCLE
            else -> SQUARE
        }

        dragImageView.setImage(
            this@MainActivity,
            imageShapeArray.random()
        )

        generateShapesOnScreen()
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
                    containerView.removeView(findViewById(viewId))
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

        var startX = 0f
        var startY = 0f

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        if (colors.size < imageShapeArray.size) {
            return
        }

        for (index in imageShapeArray.indices) {
            val imageView = ImageView(this)
            imageView.apply {
                setImage(this@MainActivity, imageShapeArray[index])

                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colors[index]));

                id = View.generateViewId();
                addedViewIds.add(id)
                containerView.addView(this)

                layoutParams.height = shapeHeight;
                layoutParams.width = shapeWidth;
                this.requestLayout();

                val xCoord = startX
                val yCoord = Random().nextInt(screenHeight - shapeHeight / 2).toFloat()

                x = xCoord
                y = yCoord

                shapesOnScreen.add(Shape(Pair(xCoord, yCoord), SQUARE))

                startX += 300
                startY += 200
            }
            setViewConstraints(imageView)
        }
    }

    companion object {
        const val shapeHeight = 150
        const val shapeWidth = 150
        const val hitFaultInPixels = 50
    }
}