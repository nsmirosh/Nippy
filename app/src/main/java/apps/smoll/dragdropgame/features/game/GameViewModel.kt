package apps.smoll.dragdropgame.features.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.*
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

    private val mutableLevelLiveData: MutableLiveData<String> = MutableLiveData()
    val levelLiveData: LiveData<String> get() = mutableLevelLiveData

    val addedViewIds = mutableSetOf<Int>()

    lateinit var timer: CountDownTimer
    var score = 0
    var sWidth = 0
    var sHeight = 0
    var level = 1

    fun startGame(screenWidthAndHeight: Pair<Int, Int>) {
        screenWidthAndHeight.apply {
            sWidth = first
            sHeight = second
        }
        buildInitialShapes()
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

    private fun buildInitialShapes() {

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
            ), level
        )
            .mapIndexed { index, shape -> Shape(shape, imageShapeArray[index], colorsArray[index]) }
            .toMutableList()
            .apply {
                mutableScreenShapesLiveData.value = this
                buildMatchingShape()
            }
    }

    private fun buildMatchingShape() {

        val oneOfTheShapesOnScreen = screenShapesLiveData.value!!.random()

        val xPos = (sWidth / 2) - shapeSize
        val yPos = (sHeight * 0.7).toInt()

        val shapeToMatch = oneOfTheShapesOnScreen.copy(
            shapeCenter = Pair(xPos, yPos),
            colorResource = R.color.shape_to_match_color
        )

        mutableShapeToMatchLiveData.value = shapeToMatch
    }

    fun handleMatchingShapeDrop(dropEventCoordinates: Pair<Int, Int>) {
        getShapeThatIsHit(dropEventCoordinates).apply {
            if (this != null) {
                onShapeHit(this)
            } else {
                updateMatchingShapePos(dropEventCoordinates)
            }
        }
    }

    private fun onShapeHit(shape: Shape) {
        if (shouldGoToNextLevel()) {
            level++
            updateLevel()
        } else {
            removeShapeThatWasHit(shape)
            score++
            updateScore()
            buildMatchingShape()
        }
    }

    private fun shouldGoToNextLevel() = screenShapesLiveData.value!!.size == 1

    private fun updateMatchingShapePos(coordinates: Pair<Int, Int>) {
        val shapeToMatch = shapeToMatchLiveData.value!!
        val coordsAdjustedForLayoutOnScreen =
            Pair(coordinates.first - halfShapeSize, coordinates.second - halfShapeSize)
        mutableShapeToMatchLiveData.value = shapeToMatch.copy(coordsAdjustedForLayoutOnScreen)
    }

    private fun removeShapeThatWasHit(shapeThatWasHit: Shape) {
        mutableScreenShapesLiveData.value =
            screenShapesLiveData.value?.filter { it.typeResource != shapeThatWasHit.typeResource }
    }

    private fun getShapeThatIsHit(targetCoordinates: Pair<Int, Int>): Shape? {
        screenShapesLiveData.value?.forEach {
            val shapeOnScreenXCenter = it.shapeCenter.first + halfShapeSize
            val shapeOnScreenYCenter = it.shapeCenter.second + halfShapeSize
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
        score = 0
        level = 1
        startGame(screenWidthAndHeight)
        updateScore()
    }

    private fun updateScore() {
        val scoreString = getApplication<GameApplication>().getString(R.string.score, score)
        mutableScoreLiveData.value = scoreString
    }

    private fun updateLevel() {
        val levelString = getApplication<GameApplication>().getString(R.string.level, level)
        mutableLevelLiveData.value = levelString
    }
}