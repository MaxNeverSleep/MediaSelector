package com.max.mediaselector

import android.provider.MediaStore.Audio.Media
import kotlin.math.max

object MediaSelectorResult {

    private var maxSelectCount: Int = 1;
    private val selectedMediaFile = ArrayList<MediaFile>()
    private val selectedMediaMap = HashMap<Long, MediaFile>()

    fun init(maxSelectCount: Int) {
        this.maxSelectCount = maxSelectCount
    }

    fun getSelectedCount(): Int {
        return selectedMediaFile.size
    }

    fun getMaxCount(): Int {
        return maxSelectCount
    }

    fun addMediaFile(mediaFile: MediaFile): Boolean {
        if (isReachMaxCount()) {
            return false
        }
        if (!selectedMediaMap.contains(mediaFile.id) && !selectedMediaFile.contains(mediaFile)) {
            selectedMediaMap[mediaFile.id] = mediaFile
            selectedMediaFile.add(mediaFile)
            return true
        }
        return false
    }

    fun removeMediaFile(mediaFile: MediaFile) {
        if (selectedMediaMap.contains(mediaFile.id)) {
            selectedMediaMap.remove(mediaFile.id)
        }
        if (!selectedMediaFile.remove(mediaFile)) {
            var needRemoveFile: MediaFile? = null
            selectedMediaFile.forEach {
                if (it.id == mediaFile.id) {
                    needRemoveFile = it
                }
            }
            selectedMediaFile.remove(needRemoveFile)
        }
    }

    fun clear() {
        selectedMediaFile.clear()
        selectedMediaMap.clear()
    }

    fun getResult(): ArrayList<MediaFile> {
        val result = ArrayList<MediaFile>()
        selectedMediaFile.forEach {
            result.add(it.copy())
        }
        return result
    }

    fun isReachMaxCount(): Boolean {
        return getSelectedCount() >= maxSelectCount
    }

}