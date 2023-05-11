package com.max.mediaselector.view

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.mediaselector.MediaFile
import com.max.mediaselector.MediaScanner
import com.max.mediaselector.MediaSelectorResult
import com.max.mediaselector.adapter.MediaSelectorAdapter
import java.util.*
import kotlin.concurrent.thread

class MediaSelectorRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mediaSelectorAdapter by lazy {
        MediaSelectorAdapter(context)
    }

    private val mediaFiles = ArrayList<MediaFile>()

    var isInitialized: Boolean = false

    fun init(
        enableImage: Boolean,
        enableVideo: Boolean,
        enableSelect: Boolean,
    ) {
        layoutManager = GridLayoutManager(context, 4)
        adapter = mediaSelectorAdapter
        loadMediaFiles(enableImage, enableVideo) { mediaFiles ->
            mediaSelectorAdapter.updateMediaData(
                enableSelect,
                mediaFiles
            )
            addItemSpacing()
            isInitialized = true
        }

    }

    fun notifyDataSetChanged() {
        mediaSelectorAdapter.notifyDataSetChanged()
    }

    private fun addItemSpacing() {
        setPadding(
            paddingLeft + TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.resources.displayMetrics
            ).toInt(), paddingTop, paddingRight + TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                context.resources.displayMetrics
            ).toInt(), paddingEnd
        )
        addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: State
            ) {
                outRect.top = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    2f,
                    context.resources.displayMetrics
                ).toInt()

                outRect.left = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    context.resources.displayMetrics
                ).toInt()
                outRect.right = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    context.resources.displayMetrics
                ).toInt()

                if (getChildLayoutPosition(view) % 4 == 0) {
                    outRect.left = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 1f,
                        context.resources.displayMetrics
                    ).toInt()
                }
                if ((getChildLayoutPosition(view) + 1) % 4 == 0) {
                    outRect.right = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 1f,
                        context.resources.displayMetrics
                    ).toInt()
                }
            }
        })
    }

    private fun loadMediaFiles(
        enableImage: Boolean,
        enableVideo: Boolean,
        callback: (ArrayList<MediaFile>) -> Unit
    ) {
        thread {
            mediaFiles.clear()
            MediaSelectorResult.clear()

            if (enableImage) {
                val images = MediaScanner.loadImages(context)
                mediaFiles.addAll(images)
            }

            if (enableVideo) {
                val videos = MediaScanner.loadVideos(context)
                mediaFiles.addAll(videos)
            }

            mediaFiles.sortWith { p0, p1 ->
                if (p0 != null && p1 != null) {
                    return@sortWith p1.timeStamp.compareTo(p0.timeStamp)
                }
                return@sortWith 0
            }

            Handler(Looper.getMainLooper()).post {
                callback.invoke(mediaFiles)
            }

        }
    }

    fun setOnSelectCountChangedListener(listener: OnSelectMediaFileListener) {
        mediaSelectorAdapter.setOnSelectCountChangedListener(listener)
    }

    fun setOnMediaItemClickListener(listener: OnMediaItemClickListener) {
        mediaSelectorAdapter.setOnMediaItemClickListener(listener)
    }

    interface OnSelectMediaFileListener {
        fun onSelect(mediaFile: MediaFile)

        fun onUnSelect(mediaFile: MediaFile)
    }

    interface OnMediaItemClickListener {
        fun onMediaItemClick(position: Int, mediaFile: MediaFile)
    }

}
