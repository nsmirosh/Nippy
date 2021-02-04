package apps.smoll.dragdropgame.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.shapeSize

fun ImageView.setShape(context: Context, shape: Shape) {

    visible()
    layoutParams =  ConstraintLayout.LayoutParams(   shapeSize,
        shapeSize)
    setImageDrawable(
        ContextCompat.getDrawable(
            context,
            shape.typeResource
        )
    )

    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(ContextCompat.getColor(context, shape.colorResource))
    )

    shape.topLeftCoords.apply {
        x = first.toFloat()
        y = second.toFloat()
    }
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}