package apps.smoll.dragdropgame.features.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.features.Level


class GameViewModel : ViewModel() {

    private val mutableScreenShapesLiveData: MutableLiveData<List<Shape>> = MutableLiveData()
    val screenShapesLiveData: LiveData<List<Shape>> get() = mutableScreenShapesLiveData

    private val mutableShapeToMatchLiveData: MutableLiveData<Shape> = MutableLiveData()
    val shapeToMatchLiveData: LiveData<Shape> get() = mutableShapeToMatchLiveData

    private val mutableLevelLiveData: MutableLiveData<List<Level>> = MutableLiveData()
    val levelLiveData: LiveData<List<Level>> get() = mutableLevelLiveData

    private val mutableScoreLiveData: MutableLiveData<Int> = MutableLiveData()
    val scoreLiveData: LiveData<Int> get() = mutableScoreLiveData

    val initialShapeToMatchCoordinates = Pair(500f, 500f)

    fun startGame() {
        buildMatchingShape()
        buildInitialShapesToMatch()
    }

    private fun buildMatchingShape() {
        mutableShapeToMatchLiveData.value = Shape(initialShapeToMatchCoordinates, 0, 0)
    }

    private fun buildInitialShapesToMatch() {
        val imageShapeArray = arrayOf(
            R.drawable.ic_square,
            R.drawable.ic_hexagonal,
            R.drawable.ic_star,
            R.drawable.ic_circle
        )

        val colorsArray = arrayOf(
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
        )

        imageShapeArray.shuffle()
        colorsArray.shuffle()

        val shapes = (imageShapeArray zip colorsArray).map {
            Shape(Pair(0f, 0f), (it.first), it.second)
        }

        mutableScreenShapesLiveData.value = shapes
    }

    fun handleDrop(coordinates: Pair<Float, Float>) {
        if (isTargetGetHit(coordinates)) {
            var previousScore: Int = scoreLiveData.value ?: 0
            mutableScoreLiveData.value = ++previousScore
        }
        moveMatchingShapeToInitialPos()
    }

    private fun moveMatchingShapeToInitialPos() {
        val shapeToMatch = shapeToMatchLiveData.value!!
        mutableShapeToMatchLiveData.value = Shape(initialShapeToMatchCoordinates, shapeToMatch.typeResource, shapeToMatch.colorResource)
    }

    private fun isTargetGetHit(targetCoordinates: Pair<Float, Float>): Boolean {
        for (shapeOnScreen in screenShapesLiveData.value!!.iterator()) {
            shapeOnScreen.apply {
                val shapeOnScreenXCenter = this.coordinates.first + MainFragment.shapeWidth / 2
                val shapeOnScreenYCenter = this.coordinates.second + MainFragment.shapeHeight / 2
                val permissibleXFaultRange =
                    shapeOnScreenXCenter - MainFragment.permissibleHitFaultInPixels..shapeOnScreenXCenter + MainFragment.permissibleHitFaultInPixels
                val permissibleYFaultRange =
                    shapeOnScreenYCenter - MainFragment.permissibleHitFaultInPixels..shapeOnScreenYCenter + MainFragment.permissibleHitFaultInPixels

                val isXHit =
                    targetCoordinates.first in permissibleXFaultRange
                val isYHit =
                    targetCoordinates.second in permissibleYFaultRange

                val shapeMatch = shapeToMatchLiveData.value!!.typeResource == typeResource
                if (isXHit && isYHit && shapeMatch) return true
            }
        }
        return false
    }

    fun restartGame() {
        mutableScoreLiveData.value = 0
    }
}