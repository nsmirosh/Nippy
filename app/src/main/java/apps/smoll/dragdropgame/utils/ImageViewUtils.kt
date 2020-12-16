package apps.smoll.dragdropgame.utils

import android.content.Context
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


fun ImageView.settleInPosition(pair: Pair<Float, Float>) {
    apply {
        x = pair.first - width / 2
        y = pair.second - height / 2
    }
}

fun ImageView.setImage(context: Context, resourceId: Int) {
    setImageDrawable(
        ContextCompat.getDrawable(
            context,
            resourceId
        )
    )
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}