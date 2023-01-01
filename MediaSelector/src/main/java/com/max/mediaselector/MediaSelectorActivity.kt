package com.max.mediaselector

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.max.mediaselector.databinding.MediaSelectorActivityBinding
import com.max.mediaselector.fragment.MediaListFragment

class MediaSelectorActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_MEDIA_FILES = "selected_media_files"
    }

    private val binding: MediaSelectorActivityBinding by lazy {
        MediaSelectorActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            finish()
            SecurityException("media selector need read external storage permission").printStackTrace()
        }

        val mediaListFragment = MediaListFragment()

        supportFragmentManager.beginTransaction()
            .add(binding.mediaSelectorFragmentContainer.id, mediaListFragment)
            .addToBackStack(MediaListFragment::class.java.name)
            .commit()

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}