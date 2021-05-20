package apps.smoll.dragdropgame.features.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import apps.smoll.dragdropgame.utils.extensions.snackBar


abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel>(val layout: Int) : Fragment() {

    protected lateinit var binding: T


    override fun onStart() {
        super.onStart()
        monitorInternet()
    }

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

    protected abstract fun getViewModelInstance(): VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewModelInstance().genericErrorMessage.observe(
            viewLifecycleOwner,
            {
                showError(it.message!!)
            }
        )
    }

    private fun monitorInternet() {
        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                // network available
            }

            override fun onLost(network: Network) {
                showError("no internet!")
            }
        }

        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }

    private fun showError(errorMessage: CharSequence) {
        view?.snackBar(errorMessage)
    }
}