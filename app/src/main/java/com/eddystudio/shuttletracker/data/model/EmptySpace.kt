package com.eddystudio.shuttletracker.data.model

data class EmptySpace(
  val padding: Int,
  val title: String?,
  val type: TYPE
) {
  companion object {
    enum class TYPE {
      SHUTTLE,
      STOP
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as EmptySpace

    if (type != other.type) return false

    return true
  }

  override fun hashCode(): Int {
    return type.hashCode()
  }

}