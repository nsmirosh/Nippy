package apps.smoll.dragdropgame.features.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.*
import apps.smoll.dragdropgame.utils.Event
import apps.smoll.dragdropgame.utils.buildShapesWithRandomColorsAndShapeTypes
import apps.smoll.dragdropgame.utils.generateNonCollidingCoordinateList


const val permissibleHitFaultInPixels = 50

const val timeLeftInMilliseconds = 10000L
const val intervalInMilliseconds = 1000L

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _screenShapes: MutableLiveData<List<Shape>> = MutableLiveData()
    val screenShapes: LiveData<List<Shape>> get() = _screenShapes

    private val _shapeToMatch: MutableLiveData<Shape> = MutableLiveData()
    val shapeToMatch: LiveData<Shape> get() = _shapeToMatch

    private val _scoreText: MutableLiveData<String> = MutableLiveData()
    val scoreText: LiveData<String> get() = _scoreText

    private val _timerText: MutableLiveData<String> = MutableLiveData()
    val timerText: LiveData<String> get() = _timerText

    private val _levelText: MutableLiveData<String> = MutableLiveData()
    val levelText: LiveData<String> get() = _levelText

    private val _userLostEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val userLostEvent: LiveData<Event<Boolean>> get() = _userLostEvent

    private val _userWonEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val userWonEvent: LiveData<Event<Boolean>> get() = _userWonEvent

    val addedViewIds = mutableSetOf<Int>()

    lateinit var timer: CountDownTimer
    private var score = 0
    private var currentLevelScore = 0
    private var sWidth = 0
    private var sHeight = 0
    private var level = 1
    private var timeLeftInSeconds = 0

    fun startGame(screenWidthAndHeight: Pair<Int, Int>) {

        with(screenWidthAndHeight) {
            sWidth = first
            sHeight = second
        }
        buildInitialShapes()
        startTimer()
    }

    private fun startTimer() {
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
        _userLostEvent.value = Event(true)
        _screenShapes.value = listOf()
        _shapeToMatch.value = null
    }

    private fun onTimerTick(millisUntilFinished: Long) {
        timeLeftInSeconds = (millisUntilFinished / intervalInMilliseconds).toInt()
        updateTimerText()
    }

    private fun buildInitialShapes() {
        val coordsList = generateCoordinatesForShapesOnScreen()
        buildShapesWithRandomColorsAndShapeTypes(coordsList)
            .apply {
                _screenShapes.value = this
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

        val oneOfTheShapesOnScreen = screenShapes.value!!.random()

        val xPos = (sWidth / 2) - shapeSize
        val yPos = (sHeight * 0.7).toInt()

        val shapeToMatch = oneOfTheShapesOnScreen.copy(
            shapeCenter = Pair(xPos, yPos),
            colorResource = R.color.shape_to_match_color
        )

        _shapeToMatch.value = shapeToMatch
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
            _userWonEvent.value = Event(true)

        } else {
            currentLevelScore++
            buildMatchingShape()
        }
        score++
        updateAllText()
    }

    private fun shouldGoToNextLevel() = screenShapes.value!!.isEmpty()

    private fun updateMatchingShapePosOnScreen(coordinates: Pair<Int, Int>) {
        val shapeToMatch = shapeToMatch.value!!
        val coordsAdjustedForLayoutOnScreen =
            Pair(coordinates.first - halfShapeSize, coordinates.second - halfShapeSize)
        _shapeToMatch.value = shapeToMatch.copy(coordsAdjustedForLayoutOnScreen)
    }

    private fun removeShapeThatWasHit(shapeThatWasHit: Shape) {
        _screenShapes.value =
            screenShapes.value?.filter { it.typeResource != shapeThatWasHit.typeResource }
    }

    private fun getShapeThatIsHit(targetCoordinates: Pair<Int, Int>): Shape? {
        screenShapes.value?.forEach {
            val shapeOnScreenXCenter = it.shapeCenter.first + halfShapeSize
            val shapeOnScreenYCenter = it.shapeCenter.second + halfShapeSize
            val permissibleXFaultRange =
                (shapeOnScreenXCenter - permissibleHitFaultInPixels)..(shapeOnScreenXCenter + permissibleHitFaultInPixels)
            val permissibleYFaultRange =
                (shapeOnScreenYCenter - permissibleHitFaultInPixels)..(shapeOnScreenYCenter + permissibleHitFaultInPixels)

            val isXHit =
                targetCoordinates.first in permissibleXFaultRange
            val isYHit =
                targetCoordinates.second in permissibleYFaultRange

            val shapeMatch = shapeToMatch.value?.typeResource == it.typeResource
            if (isXHit && isYHit && shapeMatch) return it
        }
        return null
    }

    fun restartLevel(screenWidthAndHeight: Pair<Int, Int>) {
        timer.apply {
            cancel()
            start()
        }
        startGame(screenWidthAndHeight)
        updateAllText()
    }

    private fun updateAllText() {
        updateScoreText()
        updateTimerText()
        upgradeLevelText()
    }

    private fun updateScoreText() {
        val scoreString = getApplication<GameApplication>().getString(R.string.score, score)
        _scoreText.value = scoreString
    }

    private fun upgradeLevelText() {
        val levelString = getApplication<GameApplication>().getString(R.string.level, level)
        _levelText.value = levelString
    }

    private fun updateTimerText() {
        val secondsLeftString =
            getApplication<GameApplication>().getString(R.string.time_left, timeLeftInSeconds)
        _timerText.value = secondsLeftString
    }
}