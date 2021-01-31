package apps.smoll.dragdropgame.features.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.*
import apps.smoll.dragdropgame.utils.buildShapesWithRandomColorsAndShapeTypes
import apps.smoll.dragdropgame.utils.generateNonCollidingCoordinateList


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
    var currentLevelScore = 0
    var sWidth = 0
    var sHeight = 0
    var level = 1
    var timeLeftInSeconds = 0

    fun startGame(screenWidthAndHeight: Pair<Int, Int>) {

        with(screenWidthAndHeight) {
            sWidth = first
            sHeight = second
        }
        buildInitialShapes()
        startTimer()
    }

    fun startTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        timer = object : CountDownTimer(timeLeftInMilliseconds, intervalInMilliseconds) {
            override fun onTick(millisUntilFinished: Long) = onTimerTick(millisUntilFinished)

            /*
            We cancel the timer only in once case: In case the user finished the level successfully.
              So in case it did complete - we know for sure the player failed.
             */
            override fun onFinish() = onPlayerFail()

        }.start()
    }

    private fun onPlayerFail() {
        score -= currentLevelScore
        currentLevelScore = 0
        updateScoreText()
    }

    private fun onTimerTick(millisUntilFinished: Long) {
        timeLeftInSeconds = (millisUntilFinished / intervalInMilliseconds).toInt()
        updateTimerText()
    }

    private fun buildInitialShapes() {
        val coordsList = generateCoordinatesForShapesOnScreen()
        buildShapesWithRandomColorsAndShapeTypes(coordsList)
            .apply {
                mutableScreenShapesLiveData.value = this
                buildMatchingShape()
            }
    }

    private fun generateCoordinatesForShapesOnScreen(): List<Pair<Int, Int>> {
        val widthBound = (sWidth * 0.8).toInt()
        val heightBound = (sHeight * 0.8).toInt()

        return generateNonCollidingCoordinateList(
            Pair(
                widthBound,
                heightBound
            ), level
        )
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
                removeShapeThatWasHit(this)
                onShapeHit()
            } else {
                updateMatchingShapePosOnScreen(dropEventCoordinates)
            }
        }
    }

    private fun onShapeHit() {
        if (shouldGoToNextLevel()) {
            level++
            timer.cancel()
            timeLeftInSeconds = 0
            upgradeLevel()
        } else {
            currentLevelScore++
            buildMatchingShape()
        }
        score++
        updateAllText()
    }

    private fun shouldGoToNextLevel() = screenShapesLiveData.value!!.size == 0

    private fun updateMatchingShapePosOnScreen(coordinates: Pair<Int, Int>) {
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
        updateAllText()
    }

    private fun updateAllText() {
        updateScoreText()
        updateTimerText()
    }

    private fun updateScoreText() {
        val scoreString = getApplication<GameApplication>().getString(R.string.score, score)
        mutableScoreLiveData.value = scoreString
    }

    private fun upgradeLevel() {
        val levelString = getApplication<GameApplication>().getString(R.string.level, level)
        mutableLevelLiveData.value = levelString
    }

    private fun updateTimerText() {
        val secondsLeftString =
            getApplication<GameApplication>().getString(R.string.time_left, timeLeftInSeconds)
        mutableTimeLeftLiveData.value = secondsLeftString
    }
}