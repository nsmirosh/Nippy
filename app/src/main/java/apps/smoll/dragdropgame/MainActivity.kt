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
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import apps.smoll.dragdropgame.utils.settleInPosition
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val IMAGEVIEW_TAG = "icon bitmap"

class MainActivity : AppCompatActivity() {

    val addedViewIds = mutableSetOf<Int>()
    val shapesCoordinates = mutableSetOf<Pair<Float, Float>>()

    companion object {
        const val shapeHeight = 150
        const val shapeWidth = 150
        const val hitFaultInPixels = 50
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateShapesOnScreen()

        restartGameButton.setOnClickListener {
            generateShapesOnScreen()
        }

        myImageView.setOnLongClickListener { v: View ->
            val item = ClipData.Item(v.tag as? CharSequence)
            val dragData = ClipData(
                v.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val myShadow = MyDragShadowBuilder(myImageView)
            val dragShadow = View.DragShadowBuilder(myImageView)
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
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.
                        (v as? ImageView)?.setColorFilter(Color.BLUE)

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate()

                        // returns true to indicate that the View can accept the dragged data.
                        true
                    } else {
                        // Returns false. During the current drag and drop operation, this View will
                        // not receive events again until ACTION_DRAG_ENDED is sent.
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Applies a green tint to the View. Return true; the return value is ignored.
                    (v as? ImageView)?.setColorFilter(Color.GREEN)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    // Ignore the event
                    true
                DragEvent.ACTION_DRAG_EXITED -> {
                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
                    (v as? ImageView)?.setColorFilter(Color.BLUE)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Gets the item containing the dragged data
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    handleDrop(event)
                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.

                    // Turns off any color tints
                    (v as? ImageView)?.clearColorFilter()

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Returns true. DragEvent.getResult() will return true.
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    // Turns off any color tinting
                    (v as? ImageView)?.clearColorFilter()

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Does a getResult(), and displays what happened.
                    /*   when (event.result) {
                           true ->
                               Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG)
                           else ->
                               Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG)
                       }.show()*/

                    // returns true; the value is ignored.
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
        myImageView.settleInPosition(event)

        if (isTargetGetHit(event)) {
            Toast.makeText(this, "GOOD JOB!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isTargetGetHit(event: DragEvent): Boolean {
        var isXHit = false
        var isYHit = false
        for (coordinate in shapesCoordinates) {
            coordinate.apply {
                isXHit =
                    event.x - shapeWidth / 2 in this.first - hitFaultInPixels..this.first + hitFaultInPixels
                isYHit =
                    event.y - shapeHeight / 2 in this.second - hitFaultInPixels..this.second + hitFaultInPixels
            }
        }
        return isXHit && isYHit
    }

    private fun generateShapesOnScreen() {
        fun clearPreviousViews() {
            if (addedViewIds.isNotEmpty()) {
                for (viewId in addedViewIds) {
                    containerView.removeView(findViewById(viewId))
                }
            }
            addedViewIds.clear()
            shapesCoordinates.clear()
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
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        imageShapeArray[index]
                    )
                )

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

                shapesCoordinates.add(Pair(xCoord, yCoord))

                startX += 300
                startY += 200
            }
            setViewConstraints(imageView)
        }
    }
}