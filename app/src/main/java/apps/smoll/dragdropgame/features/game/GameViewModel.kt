package apps.smoll.dragdropgame.features.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.halfShapeSize
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.shapeSize
import apps.smoll.dragdropgame.utils.Event
import apps.smoll.dragdropgame.utils.areCoordinatesHit
import apps.smoll.dragdropgame.utils.buildShapesWithRandomColorsAndShapeTypes
import apps.smoll.dragdropgame.utils.minus
import kotlinx.coroutines.launch

const val timeLeftInMilliseconds = 20000L
const val intervalInMilliseconds = 1000L


class GameViewModel(val firebaseRepo: FirebaseRepo) : ViewModel() {

    private val _screenShapes: MutableLiveData<List<Shape>> = MutableLiveData()
    val screenShapes: LiveData<List<Shape>> get() = _screenShapes

    private val _shapeToMatch: MutableLiveData<Shape> = MutableLiveData()
    val shapeToMatch: LiveData<Shape> get() = _shapeToMatch

    private val _totalScore: MutableLiveData<Int> = MutableLiveData(0)
    val totalScore: LiveData<Int> get() = _totalScore

    private val _secondsLeft: MutableLiveData<Int> = MutableLiveData(20)
    val secondsLeft: LiveData<Int> get() = _secondsLeft

    private val _currentLevel: MutableLiveData<Int> = MutableLiveData(1)
    val currentLevel: LiveData<Int> get() = _currentLevel

    private val _userLostEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val userLostEvent: LiveData<Event<Boolean>> get() = _userLostEvent

    private val _userWonEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val userWonEvent: LiveData<Event<Unit>> get() = _userWonEvent

    val addedViewIds = mutableSetOf<Int>()

    lateinit var timer: CountDownTimer

    private var levelScore = 0
    private var sWidth = 0
    private var sHeight = 0
    var levelStartTime: Long = 0

    fun startGame(width: Int, height: Int, previousLevelStats: LevelStats? = null) {
        sWidth = width
        sHeight = height
        levelStartTime = System.currentTimeMillis()
        previousLevelStats?.let { initWithPreviousLevelStats(it) }
        buildInitialShapes()
        buildMatchingShape()
        startTimer()
    }

    private fun initWithPreviousLevelStats(previousLevelStats: LevelStats) =
        with(previousLevelStats) {
            _currentLevel.value = level
            _totalScore.value = totalScore
        }

    private fun startTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        timer = object : CountDownTimer(timeLeftInMilliseconds, intervalInMilliseconds) {
            override fun onTick(millisUntilFinished: Long) {
                _secondsLeft.value = (millisUntilFinished / intervalInMilliseconds).toInt()
            }

            /*
            We cancel the timer only in once case: In case the user finished the level successfully.
              So in case it did complete - we know for sure the player failed.
             */
            override fun onFinish() = onPlayerFail()

        }.start()
    }

    private fun onPlayerFail() {
        _totalScore.value = totalScore.value!! - levelScore
        levelScore = 0
        _userLostEvent.value = Event(true)
        _screenShapes.value = listOf()
        _shapeToMatch.value = null
    }

    private fun buildInitialShapes() {
        _screenShapes.value =
            buildShapesWithRandomColorsAndShapeTypes(currentLevel.value!!, Pair(sWidth, sHeight))
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
        if (screenShapes.value!!.isEmpty()) {
            proceedToNextLevel()
        } else {
            levelScore++
            buildMatchingShape()
        }
        _totalScore.value = totalScore.value?.inc()
    }

    private fun proceedToNextLevel() {
        _currentLevel.value = _currentLevel.value?.inc()
        writeLevelDataToFirestore()
        timer.cancel()
        _secondsLeft.value = 0
        _userWonEvent.value = Event(Unit)
    }

    private fun writeLevelDataToFirestore() {

        val currentMillis = System.currentTimeMillis()

        val stats = LevelStats(
            currentMillis,
            currentMillis - levelStartTime,
            totalScore.value!!,
            levelScore,
            currentLevel.value!!,
        )

        viewModelScope.launch {
            firebaseRepo.writeLevelStats(stats)
        }
    }

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
    }
}