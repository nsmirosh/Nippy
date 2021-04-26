package apps.smoll.dragdropgame.features.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.*
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.*
import kotlinx.coroutines.launch

import timber.log.Timber

const val timeLeftInMilliseconds = 20000L
const val intervalInMilliseconds = 1000L

const val statsPath = "stats"

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

    private val _userWonEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val userWonEvent: LiveData<Event<Unit>> get() = _userWonEvent

    val firebaseRepo : FirebaseRepo = FirebaseRepoImpl()

    val addedViewIds = mutableSetOf<Int>()


    lateinit var timer: CountDownTimer
    private var score = 0
    private var currentLevelScore = 0
    private var sWidth = 0
    private var sHeight = 0
    private var level = 1
    private var timeLeftInSeconds = 0
    var levelStartTime: Long = 0


    fun startGame(width: Int, height: Int) {
        sWidth = width
        sHeight = height
        levelStartTime = System.currentTimeMillis()
        buildInitialShapes()
        updateAllText()
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
        _screenShapes.value = buildShapesWithRandomColorsAndShapeTypes(level, Pair(sWidth, sHeight))
        Timber.d("screenShapes.value[0].topLeftCoords = ${_screenShapes.value!![0].topLeftCoords}")
        buildMatchingShape()
    }

    private fun buildMatchingShape() {

        val oneOfTheShapesOnScreen = screenShapes.value!!.random()

        val xPos = (sWidth / 2) - shapeSize
        val yPos = (sHeight * 0.7).toInt()

        val shapeToMatch = oneOfTheShapesOnScreen.copy(
            topLeftCoords = Pair(xPos, yPos),
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
            proceedToNextLevel()
        } else {
            currentLevelScore++
            buildMatchingShape()
        }
        score++
        updateAllText()
    }

    private fun proceedToNextLevel() {
        level++
        writeLevelDataToFirestore()
        timer.cancel()
        timeLeftInSeconds = 0
        _userWonEvent.value = Event(Unit)
    }

    private fun writeLevelDataToFirestore() {

        val currentMillis = System.currentTimeMillis()

        val stats = LevelStats(
            currentMillis,
            currentMillis - levelStartTime,
            currentLevelScore,
            level,
        )

        viewModelScope.launch {
            firebaseRepo.writeLevelStats1(stats)
        }
    }

    private fun shouldGoToNextLevel() = screenShapes.value!!.isEmpty()

    private fun updateMatchingShapePosOnScreen(coordinates: Pair<Int, Int>) {
        val coordsToCenterTheShape = coordinates - halfShapeSize
        _shapeToMatch.value = shapeToMatch.value!!.copy(coordsToCenterTheShape)
    }

    private fun removeShapeThatWasHit(shapeThatWasHit: Shape) {
        _screenShapes.value =
            screenShapes.value?.filter { it.typeResource != shapeThatWasHit.typeResource }
    }

    private fun getShapeThatIsHit(dropEventCoordinates: Pair<Int, Int>) =
        screenShapes.value?.find {
            val shapeMatch = shapeToMatch.value?.typeResource == it.typeResource
            areCoordinatesHit(dropEventCoordinates, it.topLeftCoords) && shapeMatch
        }

    fun restartLevel(screenWidthAndHeight: Pair<Int, Int>) {
        timer.apply {
            cancel()
            start()
        }
        startGame(screenWidthAndHeight.first, screenWidthAndHeight.second)
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