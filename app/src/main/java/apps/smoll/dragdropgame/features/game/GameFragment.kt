package apps.smoll.dragdropgame.features.game

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.databinding.FragmentGameBinding
import apps.smoll.dragdropgame.features.base.BaseFragment
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.events.Event
import apps.smoll.dragdropgame.utils.ui.MyDragShadowBuilder
import apps.smoll.dragdropgame.utils.ui.invisible
import apps.smoll.dragdropgame.utils.ui.setShape
import timber.log.Timber

class GameFragment : BaseFragment<FragmentGameBinding, GameViewModel>(R.layout.fragment_game) {

    val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory()
    }

    val args: GameFragmentArgs by navArgs()
    override fun getViewModelInstance() = gameViewModel

    override fun initBindingDependencies() =
        with(binding) {
            viewmodel = gameViewModel
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startObservingLiveData()
//        initListeners()
        with(binding.containerView) {
            post {
                gameViewModel.startGame(width, height, args.levelStats)
            }
        }
    }

    private fun startObservingLiveData() {

        with(gameViewModel) {
            shapeToMatch.observe(
                viewLifecycleOwner,
                { updateShapeToMatch(it) }
            )

            screenShapes.observe(
                viewLifecycleOwner,
                { updateShapesOnScreen(it) }
            )

            levelCompletedEvent.observe(
                viewLifecycleOwner,
                ::goToInBetweenFragment
            )
        }
    }

    private fun goToInBetweenFragment(event: Event<LevelStats>) {
        view?.findNavController()
            ?.navigate(GameFragmentDirections.actionGameFragmentToInBetweenFragment(event.getContentIfNotHandled()!!))

    }

    private fun updateShapeToMatch(shape: Shape?) {
        if (shape != null) {
            addViewToContainer(shape).let {
                makeViewDraggable(it)
            }
        }
    }


    private fun makeViewDraggable(view: View) {
        with(binding) {
            view.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    ACTION_DOWN -> {
                        val item = ClipData.Item(view.tag as? CharSequence)
                        val dragData = ClipData(
                            view.tag as? CharSequence,
                            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                            item
                        )

                        val myShadow = MyDragShadowBuilder(view)
                        val dragShadow = View.DragShadowBuilder(view)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            view.startDragAndDrop(dragData, dragShadow, null, 0);
                        } else {
                            view.startDrag(
                                dragData,
                                myShadow,
                                null,
                                0
                            )
                        }
                        view.performClick()
                    }
                }
                true
            }


            val dragListen = View.OnDragListener { v, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        view.invisible()
                        event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    }
                    DragEvent.ACTION_DROP -> {


                        gameViewModel.handleMatchingShapeDrop(
                            Pair(
                                event.x.toInt(),
                                event.y.toInt()
                            )
                        )
                        v.invalidate()
                        true
                    }
                    else -> {
                        // An unknown action type was received.
                        false
                    }
                }
            }
            containerView.setOnDragListener(dragListen)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameViewModel.cleanUp()
    }

    private fun updateShapesOnScreen(shapes: List<Shape>) {
        clearPreviouslyConstructedShapes()
        shapes.forEach {
            addViewToContainer(it)
        }
        binding.containerView.post {
            Timber.d("containerView height after shapes laid out = ${binding.containerView.height}")
        }
    }


    //TODO read this https://stackoverflow.com/questions/45875491/what-is-a-receiver-in-kotlin

    private fun addViewToContainer(shape: Shape) =
        ImageView(requireContext()).apply {
            setShape(requireContext(), shape)
            id = View.generateViewId()
            gameViewModel.addedViewIds.add(id)
            requestLayout()
            binding.containerView.addView(this)
        }


    private fun clearPreviouslyConstructedShapes() {
        gameViewModel.addedViewIds.apply {
            forEach {
                with(binding) {
                    containerView.apply {
                        Timber.d("Removing view with id: $it")
                        removeView(findViewById(it))
                    }
                }
            }
        }
    }

    private val screenWidthAndHeight: Pair<Int, Int>
        get() {
            val displayMetrics = DisplayMetrics()
            requireActivity().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    display?.getRealMetrics(displayMetrics)
                } else {
                    windowManager!!.defaultDisplay.getMetrics(displayMetrics)
                }
            }

            displayMetrics.apply {
                return Pair(widthPixels, heightPixels)
            }
        }
}



