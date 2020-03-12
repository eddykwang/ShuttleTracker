package com.eddystudio.shuttletracker.ui.realtimemap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eddystudio.shuttletracker.R
import com.eddystudio.shuttletracker.data.model.EmptySpace
import com.eddystudio.shuttletracker.data.model.RoutStop
import com.eddystudio.shuttletracker.data.model.RoutVehicle
import com.eddystudio.shuttletracker.ui.realtimemap.TrackerAdapter.BaseViewHolder

class TrackerAdapter(val list: ArrayList<Any>) : RecyclerView.Adapter<BaseViewHolder>() {

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
      VHType.EMPTY_SPACE -> viewHolder = EmptySpaceVH(
          LayoutInflater.from(parent.context)
              .inflate(R.layout.empty_space_layout, parent, false)
      )
    }
    if (viewHolder == null) {
      Throwable("cannot find vh type : $viewType")
      return RouteStopVH(
          LayoutInflater.from(parent.context)
              .inflate(R.layout.empty_space_layout, parent, false)
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
      is EmptySpace -> 2
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
      VHType.EMPTY_SPACE -> (holder as EmptySpaceVH).onBind(list[position] as EmptySpace)
    }
  }

  public fun addStopsData(data: List<RoutStop>) {
    val hasTitle = hasTitle("Stops:")
    val newList = ArrayList<Any>().apply {
      if (!hasTitle) {
        add(EmptySpace(1, "Stops:"))
      }
      addAll(data)
    }
    list.addAll(newList)
    notifyDataSetChanged()
  }

  fun addVehicleData(data: List<RoutVehicle>) {

    val hasTitle = hasTitle("Shuttles:")

    val newList = ArrayList<Any>().apply {
      add(EmptySpace(1, "Shuttles:"))

      addAll(data)
    }

    list.apply {
          val iterator = this.iterator()
          while (iterator.hasNext()) {
            val nextData = iterator.next()
            if (nextData is RoutVehicle || ((nextData is EmptySpace) && nextData.title == "Shuttles:")) {
              iterator.remove()
            }
          }
        }
        .addAll(0, newList)
    notifyDataSetChanged()
  }

  private fun hasTitle(title: String): Boolean {
    var hasTitle = false
    for (d in list) {
      if (d is EmptySpace) {
        if (d.title.equals(title)) {
          hasTitle = true
          break
        }
      }
    }
    return hasTitle
  }

  fun addDataToPos(
    data: List<Any>,
    position: Int
  ) {
    list.addAll(position, data)
    notifyDataSetChanged()
//    for (pos in position until list.size) {
//      notifyItemChanged(pos)
//    }
  }

  fun removeDataByRang(
    fromPos: Int,
    toPos: Int
  ) {
    for (pos in fromPos until toPos) {
      if (pos < list.size) {
        list.removeAt(pos)
        notifyItemRemoved(pos)
      }
    }
  }

  fun removeAllRoutStop() {
    var lastIndex = 0
    for (d in list) {
      if (d is RoutStop) {
        lastIndex = list.indexOf(d)
        break
      }
    }

    for (i in 0..lastIndex) {
      if (i < list.size)
        list.remove(list[i])
//      notifyItemRemoved(i)
    }
    notifyDataSetChanged()
  }

  abstract class BaseViewHolder(itemView: View) : ViewHolder(itemView) {}

  class RouteStopVH(itemView: View) : BaseViewHolder(itemView) {
    private val routeStopNameTv = itemView.findViewById<TextView>(R.id.rout_stop_name_tv)
    private val routStopExpandBt = itemView.findViewById<ImageView>(R.id.rout_stop_expand_bt)
    fun onBind(
      data: RoutStop
    ) {
      routeStopNameTv.text = data.name
    }

  }

  class VehicleVH(itemView: View) : BaseViewHolder(itemView) {
    private val vehicleNameTv = itemView.findViewById<TextView>(R.id.rout_vehicle_name_tv)
    private val vehicleSpeedTv = itemView.findViewById<TextView>(R.id.route_vehicle_speed_tv)
    fun onBind(
      data: RoutVehicle
    ) {
      vehicleNameTv.text = "${data.name}"
      vehicleSpeedTv.text = "${data.speed.toString()} mph  ( ${data.heading} )"
    }

  }

  class EmptySpaceVH(itemView: View) : BaseViewHolder(itemView) {
    private val layout = itemView.findViewById<ConstraintLayout>(R.id.empty_space_constraint_layout)
    private val title = itemView.findViewById<TextView>(R.id.empty_space_title)
    fun onBind(
      data: EmptySpace
    ) {
      layout.layoutParams = layout.layoutParams.apply {
        height *= data.padding
        title.text = data.title
      }
    }
  }

  sealed class VHType {
    companion object {
      const val ROUT_STOP = 0
      const val VEHICLE = 1
      const val EMPTY_SPACE = 2
    }
  }

}
