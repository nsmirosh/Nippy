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
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class GameFragment : BaseFragment<FragmentGameBinding>(R.layout.fragment_game) {

    val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory(FirebaseRepoImpl(Firebase.firestore))
    }

    val args: GameFragmentArgs by navArgs()

    override fun initBindingDependencies() =
        with(binding) {
            viewmodel = gameViewModel
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startObservingLiveData()
        initListeners()
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
            ?.navigate(GameFragmentDirections.actionGameFragmentToInBetweenFragment(event.getContentIfNotHandled()))

    }

    private fun updateShapeToMatch(shape: Shape?) {
        with(binding) {
            if (shape != null) {
                dragImageView.apply {
                    setShape(requireContext(), shape)
                }
            } else {
                dragImageView.gone()
            }
        }
    }

    private fun initListeners() {

        with(binding) {
            /*nextLevelButton.setOnClickListener {
                gameViewModel.startGame(containerView.width, containerView.height)
            }
            retryButton.setOnClickListener {
                gameViewModel.restartLevel(screenWidthAndHeight)
            }


            mainMenuButton.setOnClickListener {
                view?.findNavController()
                    ?.navigate(GameFragmentDirections.actionGameFragmentToMenuFragment())
            }*/

            dragImageView.setOnTouchListener { view, motionEvent ->

                when (motionEvent.action) {

                    ACTION_DOWN -> {
                        val item = ClipData.Item(view.tag as? CharSequence)
                        val dragData = ClipData(
                            view.tag as? CharSequence,
                            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                            item
                        )

                        val myShadow = MyDragShadowBuilder(dragImageView)
                        val dragShadow = View.DragShadowBuilder(dragImageView)
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
                        dragImageView.invisible()
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

    private fun updateShapesOnScreen(shapes: List<Shape>) {
        clearPreviouslyConstructedShapes()

        for (shape in shapes) {
            ImageView(requireContext()).apply {
                setShape(requireContext(), shape)
                id = View.generateViewId()
                gameViewModel.addedViewIds.add(id)
                requestLayout()
                binding.containerView.addView(this)
            }
        }

        binding.containerView.post {
            Timber.d("containerView height after shapes laid out = ${binding.containerView.height}")
        }
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



