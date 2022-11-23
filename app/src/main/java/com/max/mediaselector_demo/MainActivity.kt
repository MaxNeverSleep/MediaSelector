package com.max.mediaselector_demo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.max.mediaselector.MediaFile
import com.max.mediaselector.MediaSelectorActivity
import com.max.mediaselector_demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 100
        )
        binding.rvMediaSelector.init(true, true, true, 9)
        binding.btnGetSelectedMedia.setOnClickListener {
            binding.btnGetSelectedMedia.text =
                binding.rvMediaSelector.getSelectedMediaFiles().size.toString()
        }
        startActivityForResult(Intent(this, MediaSelectorActivity::class.java), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bundle = data?.getBundleExtra("1")
        val mediaFiles = bundle?.getSerializable("1") as ArrayList<*>
        Toast.makeText(this, mediaFiles.size.toString(), Toast.LENGTH_LONG).show()
    }
}