package apps.smoll.dragdropgame.features.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.*
import apps.smoll.dragdropgame.features.Level
import apps.smoll.dragdropgame.utils.generateNonCollidingCoordinateList
import timber.log.Timber


const val permissibleHitFaultInPixels = 50

const val timeLeftInMilliseconds = 20000L
const val intervalInMilliseconds = 1000L

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableScreenShapesLiveData: MutableLiveData<List<Shape>> = MutableLiveData()
    val screenShapesLiveData: LiveData<List<Shape>> get() = mutableScreenShapesLiveData

    private val mutableShapeToMatchLiveData: MutableLiveData<Shape> = MutableLiveData()
    val shapeToMatchLiveData: LiveData<Shape> get() = mutableShapeToMatchLiveData

    private val mutableScoreLiveData: MutableLiveData<String> = MutableLiveData()
    val scoreLiveData: LiveData<String> get() = mutableScoreLiveData

    private val mutableTimeLeftLiveData: MutableLiveData<String> = MutableLiveData()
    val timeLeftLiveData: LiveData<String> get() = mutableTimeLeftLiveData

    val addedViewIds = mutableSetOf<Int>()

    lateinit var matchingShapeCoords: Pair<Int, Int>

    lateinit var timer: CountDownTimer
    var shapeSize = 0
    var score = 0
    var sWidth = 0
    var sHeight = 0

    fun startGame(screenWidthAndHeight: Pair<Int, Int>) {
        screenWidthAndHeight.apply {
            sWidth = first
            sHeight = second
        }
        buildShapes()
        startTimer()
    }

    fun startTimer() {
        if (this::timer.isInitialized) {
            return
        }
        timer = object : CountDownTimer(timeLeftInMilliseconds, intervalInMilliseconds) {
            override fun onTick(millisUntilFinished: Long) = onTimerTick(millisUntilFinished)

            override fun onFinish() {
                Timber.d("onFinish!")
            }
        }.start()
    }

    private fun onTimerTick(millisUntilFinished: Long) {
        val secondsLeft = (millisUntilFinished / intervalInMilliseconds).toInt()
        val secondsLeftString =
            getApplication<GameApplication>().getString(R.string.time_left, secondsLeft)
        mutableTimeLeftLiveData.value = secondsLeftString
    }

    private fun buildShapes() {

        val colorsArray = arrayOf(
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
        )

        val imageShapeArray = arrayOf(
            R.drawable.ic_square,
            R.drawable.ic_hexagonal,
            R.drawable.ic_star,
            R.drawable.ic_circle
        )

        //TODO fix this later - this might not work for small screens because the shape is fixed right now  i.e. 150px
        val widthBound = (sWidth * 0.8).toInt()
        val heightBound = (sHeight * 0.8).toInt()

        generateNonCollidingCoordinateList(
            Pair(
                widthBound,
                heightBound
            ), 4
        )
            .mapIndexed { index, shape -> Shape(shape, imageShapeArray[index], colorsArray[index]) }
            .toMutableList()
            .apply {
                buildMatchingShape(this)
                mutableScreenShapesLiveData.value = this
            }
    }

    private fun buildMatchingShape(shapesThatWillBeOnScreen: MutableList<Shape>) {

        val xPos = (sWidth / 2) - shapeSize
        val yPos = (sHeight * 0.8).toInt()

        matchingShapeCoords = Pair(xPos, yPos)

        shapesThatWillBeOnScreen.shuffle()

        val shapeToMatch =
            Shape(
                matchingShapeCoords,
                shapesThatWillBeOnScreen.first().typeResource,
                shapesThatWillBeOnScreen.first().colorResource
            )
        mutableShapeToMatchLiveData.value = shapeToMatch
    }

    fun handleDrop(coordinates: Pair<Int, Int>) {
        getShapeThatIsHit(coordinates).apply {
            if (this != null) {
                score++
                updateScore()
                mutableScreenShapesLiveData.value =
                    screenShapesLiveData.value?.filter { it.typeResource != this.typeResource }
            }
        }

        moveMatchingShapeToInitialPos()
    }

    private fun moveMatchingShapeToInitialPos() {
        val shapeToMatch = shapeToMatchLiveData.value!!
        mutableShapeToMatchLiveData.value = Shape(
            matchingShapeCoords,
            shapeToMatch.typeResource,
            shapeToMatch.colorResource
        )
    }

    private fun getShapeThatIsHit(targetCoordinates: Pair<Int, Int>): Shape? {
        screenShapesLiveData.value?.forEach {
            val shapeOnScreenXCenter = it.shapeCenter.first + shapeSize / 2
            val shapeOnScreenYCenter = it.shapeCenter.second + shapeSize / 2
            val permissibleXFaultRange =
                shapeOnScreenXCenter - permissibleHitFaultInPixels..shapeOnScreenXCenter + permissibleHitFaultInPixels
            val permissibleYFaultRange =
                shapeOnScreenYCenter - permissibleHitFaultInPixels..shapeOnScreenYCenter + permissibleHitFaultInPixels

            val isXHit =
                targetCoordinates.first in permissibleXFaultRange
            val isYHit =
                targetCoordinates.second in permissibleYFaultRange

            val shapeMatch = shapeToMatchLiveData.value?.typeResource == it.typeResource
            if (isXHit && isYHit && shapeMatch) return it
        }
        return null
    }

    fun restartGame(screenWidthAndHeight: Pair<Int, Int>) {
        timer.apply {
            cancel()
            start()
        }
        startGame(screenWidthAndHeight)
        score = 0
        updateScore()
    }

    private fun updateScore() {
        val scoreString = getApplication<GameApplication>().getString(R.string.score, score)
        mutableScoreLiveData.value = scoreString
    }
}