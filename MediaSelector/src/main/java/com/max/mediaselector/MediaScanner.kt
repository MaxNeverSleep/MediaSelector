package com.max.mediaselector

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread

/**
 * MediaScanner
 *
 */
object MediaScanner {

    private const val ASCENDING = "ASC"
    private const val DESCENDING = "DESC"

    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE
    )

    private val VIDEO_PROJECTION = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DURATION
    )

    fun loadImages(context: Context): ArrayList<MediaFile> {
        return queryImages(context.contentResolver)
    }

    fun loadVideos(context: Context): ArrayList<MediaFile> {
        return queryVideo(context.contentResolver)
    }

    private fun getCursor(
        resolver: ContentResolver,
        uri: Uri,
        projection: Array<String>
    ): Cursor? {
        val sortOrder = MediaStore.MediaColumns.DATE_ADDED + " " + DESCENDING
        return resolver.query(uri, projection, null, null, sortOrder)
    }

    private fun queryVideo(resolver: ContentResolver): ArrayList<MediaFile> {
        val videoList = ArrayList<MediaFile>()
        getCursor(
            resolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            VIDEO_PROJECTION
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[0])
            val dataColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[1])
            val dateTakenColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[2])
            val nameColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[3])
            val sizeColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[4])
            val durationColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[5])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn)
                val timestamp = cursor.getLong(dateTakenColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getLong(durationColumn)
                val size = cursor.getLong(sizeColumn)

                videoList += MediaFile(
                    MediaFile.MediaType.VIDEO,
                    id,
                    name,
                    path,
                    size,
                    timestamp,
                    duration
                )
            }
        }
        return videoList
    }

    private fun queryImages(resolver: ContentResolver): ArrayList<MediaFile> {
        val imageList = ArrayList<MediaFile>()
        getCursor(
            resolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0])
            val dataColumn = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1])
            val dateTakenColumn = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2])
            val nameColumn = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3])
            val sizeColumn = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn)
                val timestamp = cursor.getLong(dateTakenColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)

                imageList += MediaFile(
                    MediaFile.MediaType.IMAGE,
                    id,
                    name,
                    path,
                    size,
                    timestamp,
                    0L
                )
            }
        }
        return imageList
    }


}