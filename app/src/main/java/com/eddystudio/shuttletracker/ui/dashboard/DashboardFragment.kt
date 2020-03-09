package com.studio.eddy.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions.Callback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eddystudio.shuttletracker.OnBackPressed
import com.eddystudio.shuttletracker.R
import com.eddystudio.shuttletracker.R.layout
import kotlinx.android.synthetic.main.fragment_dashboard.real_time_swipe_refresh_bt
import kotlinx.android.synthetic.main.fragment_dashboard.real_time_web_view

class DashboardFragment : Fragment() {

  inner class GeoWebChromeClient : WebChromeClient() {
    override fun onGeolocationPermissionsShowPrompt(
      origin: String,
      callback: Callback
    ) {
      // Always grant permission since the app itself requires location
      // permission and the user has therefore already granted it
      callback.invoke(origin, true, false)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(layout.fragment_dashboard, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner, object : OnBackPressedCallback(!real_time_web_view.canGoBack()) {
      override fun handleOnBackPressed() {
        if (real_time_web_view.canGoBack()) {
          real_time_web_view.goBack()
        } else {
          this.isEnabled = false
          findNavController().navigateUp()
        }
      }
    })

    real_time_web_view.apply {
      getSettings().javaScriptEnabled = true
      getSettings().setGeolocationEnabled(true)
      setWebChromeClient(GeoWebChromeClient())
    }

    val url = "https://www.ucsdbus.com/m/routes"
    load_web(url)

    real_time_swipe_refresh_bt.setOnRefreshListener {
      reloadPage()
    }
  }

  fun load_web(url: String?) {
    real_time_web_view.loadUrl(url)
    loading()
  }

  fun loading() {
    real_time_swipe_refresh_bt.isRefreshing = true
    real_time_web_view.webViewClient = object : WebViewClient() {
      override fun onPageFinished(
        view: WebView,
        url: String
      ) {
        if (real_time_web_view != null) {
          real_time_swipe_refresh_bt.isRefreshing = false
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
    real_time_web_view.stopLoading()
  }

  fun reloadPage() {
    //  hasInternet = false;
    if (real_time_web_view != null) {
      real_time_web_view.reload()
      loading()
    }
  }
}
