package apps.smoll.dragdropgame.features.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.features.Level
import apps.smoll.dragdropgame.shapeHeight
import apps.smoll.dragdropgame.shapeWidth
import apps.smoll.dragdropgame.utils.generateNonCollidingCoordinateList


const val permissibleHitFaultInPixels = 50


class GameViewModel : ViewModel() {

    private val mutableScreenShapesLiveData: MutableLiveData<List<Shape>> = MutableLiveData()
    val screenShapesLiveData: LiveData<List<Shape>> get() = mutableScreenShapesLiveData

    private val mutableShapeToMatchLiveData: MutableLiveData<Shape> = MutableLiveData()
    val shapeToMatchLiveData: LiveData<Shape> get() = mutableShapeToMatchLiveData

    private val mutableLevelLiveData: MutableLiveData<List<Level>> = MutableLiveData()
    val levelLiveData: LiveData<List<Level>> get() = mutableLevelLiveData

    private val mutableScoreLiveData: MutableLiveData<Int> = MutableLiveData()
    val scoreLiveData: LiveData<Int> get() = mutableScoreLiveData

    val initialShapeToMatchCoordinates = Pair(500, 500)

    fun startGame(screenWidthAndHeight: Pair<Int, Int>) {
        buildMatchingShape()
        buildShapesToMatch(screenWidthAndHeight)
    }

    private fun buildMatchingShape() {
        mutableShapeToMatchLiveData.value = Shape(
            initialShapeToMatchCoordinates,
            randomShapeResource,
            randomColorResource
        )
    }

    private fun buildShapesToMatch(screenWidthAndHeight: Pair<Int, Int>) {
        val list = mutableListOf<Shape>()
        generateNonCollidingCoordinateList(screenWidthAndHeight, 5).apply {
            forEach {
                list.add(Shape(it, randomShapeResource, randomColorResource))
            }
        }
        mutableScreenShapesLiveData.value = list
    }

    private val randomShapeResource: Int
        get() {
            val imageShapeArray = arrayOf(
                R.drawable.ic_square,
                R.drawable.ic_hexagonal,
                R.drawable.ic_star,
                R.drawable.ic_circle
            )

            imageShapeArray.shuffle()

            return imageShapeArray.first()
        }

    private val randomColorResource: Int
        get() {
            val colorsArray = arrayOf(
                R.color.color_1,
                R.color.color_2,
                R.color.color_3,
                R.color.color_4,
            )

            colorsArray.shuffle()

            return colorsArray.first()
        }

    fun handleDrop(coordinates: Pair<Int, Int>) {
        if (isTargetGetHit(coordinates)) {
            var previousScore: Int = scoreLiveData.value ?: 0
            mutableScoreLiveData.value = ++previousScore
        }
        moveMatchingShapeToInitialPos()
    }

    private fun moveMatchingShapeToInitialPos() {
        val shapeToMatch = shapeToMatchLiveData.value!!
        mutableShapeToMatchLiveData.value = Shape(
            initialShapeToMatchCoordinates,
            shapeToMatch.typeResource,
            shapeToMatch.colorResource
        )
    }

    private fun isTargetGetHit(targetCoordinates: Pair<Int, Int>): Boolean {
        for (shapeOnScreen in screenShapesLiveData.value!!.iterator()) {
            shapeOnScreen.apply {
                val shapeOnScreenXCenter = this.shapeCenter.first + shapeWidth / 2
                val shapeOnScreenYCenter = this.shapeCenter.second + shapeHeight / 2
                val permissibleXFaultRange =
                    shapeOnScreenXCenter - permissibleHitFaultInPixels..shapeOnScreenXCenter + permissibleHitFaultInPixels
                val permissibleYFaultRange =
                    shapeOnScreenYCenter - permissibleHitFaultInPixels..shapeOnScreenYCenter + permissibleHitFaultInPixels

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

    fun restartGame(screenWidthAndHeight: Pair<Int, Int>) {
        startGame(screenWidthAndHeight)
        mutableScoreLiveData.value = 0
    }
}