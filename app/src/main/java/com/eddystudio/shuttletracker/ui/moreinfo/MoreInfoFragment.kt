package com.studio.eddy.myapplication.ui.notifications

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eddystudio.shuttletracker.BuildConfig
import com.eddystudio.shuttletracker.R
import kotlinx.android.synthetic.main.fragment_more_info.more_info_call_office_bt
import kotlinx.android.synthetic.main.fragment_more_info.more_info_email_shuttle
import kotlinx.android.synthetic.main.fragment_more_info.more_info_private_policy
import kotlinx.android.synthetic.main.fragment_more_info.more_info_rate_app
import kotlinx.android.synthetic.main.fragment_more_info.more_info_send_feedback
import kotlinx.android.synthetic.main.fragment_more_info.more_info_shuttle_website
import kotlinx.android.synthetic.main.fragment_more_info.more_info_version

class MoreInfoFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_more_info, container, false)

    return root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    more_info_version.text = "Version ${BuildConfig.VERSION_NAME}"

    more_info_rate_app.setOnClickListener {
      startActivity(
          Intent(
              Intent.ACTION_VIEW, Uri.parse(
              "https://play.google.com/store/apps/details?id=com.eddystudio.shuttletracker"
          )
          )
      )
    }

    more_info_call_office_bt.setOnClickListener {
      val callIntent = Intent(Intent.ACTION_CALL)
      callIntent.data = Uri.parse("tel:(858) 534-7422")
      startActivity(callIntent)
    }

    more_info_shuttle_website.setOnClickListener {
      startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse("https://transportation.ucsd.edu/shuttles/"))
      )
    }

    more_info_send_feedback.setOnClickListener {
      val i = Intent(Intent.ACTION_SEND)
      i.type = "message/rfc822"
      i.putExtra(Intent.EXTRA_EMAIL, arrayOf("eddy.studio.dev@gmail.com"))
      try {
        startActivity(Intent.createChooser(i, "Send mail..."))
      } catch (ex: ActivityNotFoundException) {
        Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT)
            .show()
      }
    }

    more_info_email_shuttle.setOnClickListener {
      val i = Intent(Intent.ACTION_SEND)
      i.type = "message/rfc822"
      i.putExtra(Intent.EXTRA_EMAIL, arrayOf("shuttles@ucsd.edu"))
      try {
        startActivity(Intent.createChooser(i, "Send mail..."))
      } catch (ex: ActivityNotFoundException) {
        Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT)
            .show()
      }
    }

    more_info_private_policy.setOnClickListener {
      Intent(
          Intent.ACTION_VIEW,
          Uri.parse("https://eddykwang.github.io/eddystudio/ppucsdshuttletracker.html")
      )
    }
  }
}
