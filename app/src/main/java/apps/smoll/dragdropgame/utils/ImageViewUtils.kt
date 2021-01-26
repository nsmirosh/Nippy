package apps.smoll.dragdropgame.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.shapeSize
import kotlinx.android.synthetic.main.activity_main.*


fun ImageView.settleInPosition(pair: Pair<Float, Float>) {
    apply {
        x = pair.first - width / 2
        y = pair.second - height / 2
    }
}

fun ImageView.setShape(context: Context, shape: Shape) {
    layoutParams = ViewGroup.LayoutParams(
        WRAP_CONTENT,
        WRAP_CONTENT
    )
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


    shape.shapeCenter.apply {
        x = first.toFloat()
        y = second.toFloat()
    }

    layoutParams.height = shapeSize
    layoutParams.width = shapeSize


}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}