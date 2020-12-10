package apps.smoll.dragdropgame.utils

import android.view.DragEvent
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*


fun ImageView.settleInPosition(event: DragEvent) {
    apply {
        x = event.x - width / 2
        y = event.y - height / 2
    }
}