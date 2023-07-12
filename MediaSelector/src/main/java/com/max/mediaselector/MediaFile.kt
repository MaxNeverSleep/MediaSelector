package com.max.mediaselector

import android.os.Parcel
import android.os.Parcelable

class MediaFile(
    val mediaType: MediaType,
    val id: Long,
    val path: String,
    val size: Long,
    val timeStamp: Long,
    val duration: Long
) : Parcelable {

    var checked: Boolean = false

    constructor(parcel: Parcel) : this(
        MediaType.valueOf(parcel.readString() ?: MediaType.IMAGE.name),
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    enum class MediaType {
        IMAGE,
        VIDEO
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.mediaType.name)
        dest?.writeLong(this.id)
        dest?.writeString(this.path)
        dest?.writeLong(this.size)
        dest?.writeLong(this.timeStamp)
        dest?.writeLong(this.duration)
    }


    companion object CREATOR : Parcelable.Creator<MediaFile> {
        override fun createFromParcel(parcel: Parcel): MediaFile {
            return MediaFile(parcel)
        }

        override fun newArray(size: Int): Array<MediaFile?> {
            return arrayOfNulls(size)
        }
    }

    fun copy(): MediaFile {
        return MediaFile(
            mediaType,
            id,
            path,
            size,
            timeStamp,
            duration
        )
    }

}