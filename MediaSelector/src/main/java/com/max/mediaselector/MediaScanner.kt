package com.max.mediaselector

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

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
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.SIZE
    )

    private val VIDEO_PROJECTION = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DATE_MODIFIED,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DURATION
    )

    private var localImageCaches = ArrayList<MediaFile>()
    private var localVideoCaches = ArrayList<MediaFile>()

    fun getLoadedImagesCache(): ArrayList<MediaFile> {
        return localImageCaches
    }

    fun getLoadedVideoCache(): ArrayList<MediaFile> {
        return localVideoCaches
    }

    fun loadImages(context: Context): ArrayList<MediaFile> {
        localImageCaches = queryImages(context.contentResolver)
        return localImageCaches
    }

    fun loadVideos(context: Context): ArrayList<MediaFile> {
        localVideoCaches = queryVideo(context.contentResolver)
        return localVideoCaches
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
            val sizeColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[3])
            val durationColumn = cursor.getColumnIndexOrThrow(VIDEO_PROJECTION[4])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn)
                val timestamp = cursor.getLong(dateTakenColumn)
                val duration = cursor.getLong(durationColumn)
                val size = cursor.getLong(sizeColumn)

                videoList += MediaFile(
                    MediaFile.MediaType.VIDEO,
                    id,
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
            val sizeColumn = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn)
                val timestamp = cursor.getLong(dateTakenColumn)
                val size = cursor.getLong(sizeColumn)

                imageList += MediaFile(
                    MediaFile.MediaType.IMAGE,
                    id,
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