package apps.smoll.dragdropgame.features.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.halfShapeSize
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val timeLeftInMilliseconds = 20000L
const val intervalInMilliseconds = 1000L


class GameViewModel(val firebaseRepo: FirebaseRepo) : BaseViewModel() {

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

    private val _levelCompletedEvent: MutableLiveData<Event<LevelStats>> = MutableLiveData()
    val levelCompletedEvent: LiveData<Event<LevelStats>> get() = _levelCompletedEvent

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
            _currentLevel.value = levelToBePlayed
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
        _screenShapes.value = listOf()
        _shapeToMatch.value = null
        _levelCompletedEvent.value = Event(buildStatsWithLevelChanges())
    }

    private fun buildInitialShapes() {
        _screenShapes.value =
            buildShapesWithRandomColorsAndShapeTypes(currentLevel.value!!, Pair(sWidth, sHeight))
    }

    private fun buildMatchingShape() {
        _shapeToMatch.value = copyAndModifyRandomShapeFrom(screenShapes.value!!, sWidth, sHeight)
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
        _totalScore.value = totalScore.value?.inc()
        if (screenShapes.value!!.isEmpty()) {
            proceedToNextLevel()
        } else {
            levelScore++
            buildMatchingShape()
        }
    }

    private fun proceedToNextLevel() {
        _currentLevel.value = _currentLevel.value!!.inc()
        val newStats = buildStatsWithLevelChanges()
        newStats.wonCurrentLevel = true
        _levelCompletedEvent.value = Event(newStats)
        writeLevelDataToFirestore(newStats)
        timer.cancel()
        _secondsLeft.value = 0
    }

    private fun writeLevelDataToFirestore(levelStats: LevelStats) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepo.writeLevelStats(levelStats)
        }
    }

    private fun buildStatsWithLevelChanges() = LevelStats(
        System.currentTimeMillis(),
        System.currentTimeMillis() - levelStartTime,
        totalScore.value!!,
        levelScore,
        currentLevel.value!!
    )

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
}