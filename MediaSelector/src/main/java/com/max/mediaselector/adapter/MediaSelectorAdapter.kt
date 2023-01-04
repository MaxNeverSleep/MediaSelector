package com.max.mediaselector.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.max.mediaselector.MediaFile
import com.max.mediaselector.R
import com.max.mediaselector.view.MediaSelectorRecyclerView
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * MediaSelectorAdapter
 */
class MediaSelectorAdapter(private val context: Context) :
    RecyclerView.Adapter<MediaSelectorAdapter.MediaSelectorViewHolder>() {

    private var mediaFiles: List<MediaFile>? = null

    private var enableSelect: Boolean = false
    private var maxSelectCount: Int = 0
    private var selectedMediaFiles = ArrayList<MediaFile>()

    private var onSelectMediaFileListener:
            MediaSelectorRecyclerView.OnSelectMediaFileListener? = null

    private var onClickMediaItemListener:
            MediaSelectorRecyclerView.OnMediaItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaSelectorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_selector_item_media, parent, false)
        return MediaSelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaSelectorViewHolder, position: Int) {
        mediaFiles?.get(position)?.let { mediaFile ->


            //Video Duration (if it's video)
            if (mediaFile.mediaType == MediaFile.MediaType.VIDEO) {
                //Video's Cover
                Glide.with(context)
                    .load(mediaFile.path)
                    .apply(RequestOptions().frame(0))
                    .into(holder.ivCover)

                holder.rlVideoFlag.visibility = View.VISIBLE
                holder.tvDuration.text = formatDuration(mediaFile.duration)
                holder.ivCover.setOnClickListener {
                    onClickMediaItemListener?.onMediaItemClick(position, mediaFile)
                }
            } else {
                //Image
                Glide.with(context)
                    .load(mediaFile.path)
                    .into(holder.ivCover)

                holder.rlVideoFlag.visibility = View.GONE
                holder.ivCover.setOnClickListener {
                    onClickMediaItemListener?.onMediaItemClick(position, mediaFile)
                }
            }

            //CheckBox (if select function is enable)
            if (enableSelect && maxSelectCount > 0) {
                //checkbox
                holder.ivCheckBox.isSelected = mediaFile.checked

                //translucence mask
                holder.vMask.visibility = if (holder.ivCheckBox.isSelected) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                //expand click area
                holder.vCheckBoxClickArea.setOnClickListener {
                    holder.ivCheckBox.performClick()
                }

                holder.ivCheckBox.setOnClickListener {
                    val selected = holder.ivCheckBox.isSelected
                    //if reach the max count ,return
                    if (!selected && (selectedMediaFiles.size >= maxSelectCount)) {
                        return@setOnClickListener
                    }

                    holder.ivCheckBox.isSelected = !selected
                    mediaFile.checked = holder.ivCheckBox.isSelected

                    //translucence mask
                    fadeInOrOut(holder.ivCheckBox.isSelected, holder.vMask)

                    //selected - > add file to list
                    if (holder.ivCheckBox.isSelected) {
                        selectedMediaFiles.add(mediaFile)
                        onSelectMediaFileListener?.onSelect(mediaFile)
                    } else {
                        //unselected - > remove file from list
                        if (selectedMediaFiles.contains(mediaFile)) {
                            selectedMediaFiles.remove(mediaFile)
                        }
                        onSelectMediaFileListener?.onUnSelect(mediaFile)
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mediaFiles?.size ?: 0
    }

    fun updateMediaData(
        enableSelect: Boolean,
        maxSelectCount: Int,
        mediaFiles: ArrayList<MediaFile>
    ) {
        this.enableSelect = enableSelect
        this.maxSelectCount = maxSelectCount
        this.mediaFiles = mediaFiles
        notifyItemChanged(0, mediaFiles.size)
    }

    fun setOnSelectCountChangedListener(listener: MediaSelectorRecyclerView.OnSelectMediaFileListener) {
        this.onSelectMediaFileListener = listener
    }

    fun setOnMediaItemClickListener(listener: MediaSelectorRecyclerView.OnMediaItemClickListener) {
        this.onClickMediaItemListener = listener
    }

    fun getSelectedMediaFiles(): ArrayList<MediaFile> {
        return selectedMediaFiles
    }

    private fun formatDuration(duration: Long): String {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(duration)
            )
        )
    }

    private fun fadeInOrOut(selected: Boolean, view: View) {
        if (selected && view.visibility == View.GONE) {
            val animation: Animation = AlphaAnimation(0f, 1f)
            animation.duration = 100
            view.startAnimation(animation)
            view.visibility = View.VISIBLE
        }
        if (!selected && view.visibility == View.VISIBLE) {
            val animation: Animation = AlphaAnimation(1f, 0f)
            animation.duration = 100
            view.startAnimation(animation)
            view.visibility = View.GONE
        }
    }


    class MediaSelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCover: ImageView = itemView.findViewById(R.id.media_selector_iv_cover)
        val tvDuration: TextView = itemView.findViewById(R.id.media_selector_tv_duration)
        val rlVideoFlag: RelativeLayout = itemView.findViewById(R.id.media_selector_rl_video_flag)
        val ivCheckBox: ImageView = itemView.findViewById(R.id.media_selector_check_box)
        val vCheckBoxClickArea: View =
            itemView.findViewById(R.id.media_selector_check_box_click_area)
        val vMask: View = itemView.findViewById(R.id.media_selector_v_mask)
    }

}