package com.eddystudio.shuttletracker.ui.realtimemap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eddystudio.shuttletracker.R
import com.eddystudio.shuttletracker.data.model.RoutStop
import com.eddystudio.shuttletracker.data.model.RoutVehicle
import com.eddystudio.shuttletracker.ui.realtimemap.TrackerAdapter.BaseViewHolder

class TrackerAdapter : RecyclerView.Adapter<BaseViewHolder>() {

  private lateinit var list: List<Any>

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): BaseViewHolder {
    var viewHolder: BaseViewHolder? = null
    when (viewType) {
      VHType.ROUT_STOP -> viewHolder = RouteStopVH(
          LayoutInflater.from(parent.context)
              .inflate(R.layout.rout_stop_item_view, parent, false)
      )
      VHType.VEHICLE -> viewHolder = VehicleVH(
          LayoutInflater.from(parent.context)
              .inflate(R.layout.rout_vehicle_item_view, parent, false)
      )
    }
    if (viewHolder == null) {
      Throwable("cannot find vh type : $viewType")
      return RouteStopVH(
          LayoutInflater.from(parent.context)
              .inflate(R.layout.rout_stop_item_view, parent, false)
      )
    }
    return viewHolder
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun getItemViewType(position: Int): Int {
    return when (list[position]) {
      is RoutStop -> 0
      is RoutVehicle -> 1
      else -> -1
    }
  }

  override fun onBindViewHolder(
    holder: BaseViewHolder,
    position: Int
  ) {

    when (holder.itemViewType) {
      VHType.VEHICLE -> (holder as VehicleVH).onBind(list[position] as RoutVehicle)
      VHType.ROUT_STOP -> (holder as RouteStopVH).onBind(list[position] as RoutStop)
    }
  }

  public fun setData(data: List<Any>) {
    list = data
    this.notifyDataSetChanged()
  }

  abstract class BaseViewHolder(itemView: View) : ViewHolder(itemView) {}

  class RouteStopVH(itemView: View) : BaseViewHolder(itemView) {
    fun onBind(
      data: RoutStop
    ) {
    }

  }

  class VehicleVH(itemView: View) : BaseViewHolder(itemView) {
    private val vehicleNameTv = itemView.findViewById<TextView>(R.id.rout_vehicle_name_tv)
    private val vehicleSpeedTv = itemView.findViewById<TextView>(R.id.route_vehicle_speed_tv)
    fun onBind(
      data: RoutVehicle
    ) {
      vehicleNameTv.text = data.name
      vehicleSpeedTv.text = data.speed.toString()
    }

  }

  sealed class VHType {
    companion object {
      const val ROUT_STOP = 0
      const val VEHICLE = 1
    }
  }

}
