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

    private val mutableLevelLiveData: MutableLiveData<List<Level>> = MutableLiveData()
    val levelLiveData: LiveData<List<Level>> get() = mutableLevelLiveData

    private val mutableScoreLiveData: MutableLiveData<String> = MutableLiveData()
    val scoreLiveData: LiveData<String> get() = mutableScoreLiveData

    private val mutableTimeLeftLiveData: MutableLiveData<String> = MutableLiveData()
    val timeLeftLiveData: LiveData<String> get() = mutableTimeLeftLiveData

    val initialShapeToMatchCoordinates = Pair(500, 500)

    lateinit var timer: CountDownTimer
    var shapeSize = 0
    var score = 0

    fun startGame(screenWidthAndHeight: Pair<Int, Int>) {
        buildShapes(screenWidthAndHeight)
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

    private fun buildShapes(screenWidthAndHeight: Pair<Int, Int>) {

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


        generateNonCollidingCoordinateList(
            Pair(
                screenWidthAndHeight.first - 200,
                screenWidthAndHeight.second - 400
            ), 4
        )
            .mapIndexed { index, shape -> Shape(shape, imageShapeArray[index], colorsArray[index], shapeSize) }
            .toMutableList()
            .apply {

                mutableScreenShapesLiveData.value = this
                buildMatchingShape(this)
            }
    }

    private fun buildMatchingShape(shapesThatWillBeOnScreen: MutableList<Shape>) {
        shapesThatWillBeOnScreen.shuffle()

        val shapeToMatch =
            Shape(
                Pair(500, 500),
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
            initialShapeToMatchCoordinates,
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

            val shapeMatch = shapeToMatchLiveData.value!!.typeResource == it.typeResource
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