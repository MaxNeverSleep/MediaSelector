package com.max.mediaselector

import android.os.Parcelable
import java.io.Serializable

class MediaFile(
    val mediaType: MediaType,
    val id: Long,
    val name: String,
    val path: String,
    val size: Long,
    val timeStamp: Long,
    val duration: Long
) : Serializable {

    var checked: Boolean = false

    enum class MediaType {
        IMAGE,
        VIDEO
    }

}