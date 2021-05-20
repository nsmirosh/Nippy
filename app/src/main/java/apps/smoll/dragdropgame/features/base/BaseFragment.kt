package apps.smoll.dragdropgame.features.base

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import apps.smoll.dragdropgame.utils.extensions.longToast

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel>(val layout: Int) : Fragment() {

    protected lateinit var binding: T
    protected abstract val viewModelInstance: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initBindingDependencies()
        return binding.root
    }

    protected abstract fun initBindingDependencies()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInstance.genericErrorMessage.observe(
            viewLifecycleOwner,
            {
                context?.longToast(it.message!!)
            }
        )
    }
}