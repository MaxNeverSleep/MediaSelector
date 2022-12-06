package com.max.mediaselector_demo

import android.Manifest
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.max.mediaselector.MediaFile
import com.max.mediaselector.MediaSelectorActivity
import com.max.mediaselector_demo.databinding.ActivityMainBinding
import com.max.mediaselector_demo.databinding.ItemSelectedResultBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    private val adapter by lazy {
        SelectedResultAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvSelectResult.layoutManager = GridLayoutManager(this, 4)
        binding.rvSelectResult.adapter = adapter
        addItemSpacing()

        binding.btnOpenMediaSelector.setOnClickListener {
            if (PermissionUtils.isPermissionGranted(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                openSelectorLauncher.launch(Intent(this, MediaSelectorActivity::class.java))
            } else {
                permissionRequestLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    class SelectedResultAdapter(private val activity: FragmentActivity) :
        RecyclerView.Adapter<SelectedResultAdapter.SelectedResultViewHolder>() {

        private val mediaFiles = ArrayList<MediaFile>()

        class SelectedResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding: ItemSelectedResultBinding by lazy {
                ItemSelectedResultBinding.bind(itemView)
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SelectedResultViewHolder {
            return SelectedResultViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_selected_result, parent, false)
            )
        }

        override fun onBindViewHolder(holder: SelectedResultViewHolder, position: Int) {
            Glide.with(activity)
                .load(mediaFiles[position].path)
                .into(holder.binding.ivCover)
        }

        override fun getItemCount(): Int {
            return mediaFiles.size
        }

        fun setMediaFiles(mediaFiles: ArrayList<MediaFile>?) {
            if (mediaFiles != null) {
                this.mediaFiles.clear()
                this.mediaFiles.addAll(mediaFiles)
                notifyDataSetChanged()
            }
        }

    }

    private fun addItemSpacing() {
        binding.rvSelectResult.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    2f,
                    this@MainActivity.resources.displayMetrics
                ).toInt()

                outRect.left = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    this@MainActivity.resources.displayMetrics
                ).toInt()
                outRect.right = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    this@MainActivity.resources.displayMetrics
                ).toInt()

                if (binding.rvSelectResult.getChildLayoutPosition(view) % 4 == 0) {
                    outRect.left = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 1f,
                        this@MainActivity.resources.displayMetrics
                    ).toInt()
                }
                if ((binding.rvSelectResult.getChildLayoutPosition(view) + 1) % 4 == 0) {
                    outRect.right = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 1f,
                        this@MainActivity.resources.displayMetrics
                    ).toInt()
                }
            }
        })
    }

    private val openSelectorLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val mediaFiles =
                result.data?.getParcelableArrayListExtra<MediaFile>(MediaSelectorActivity.SELECTED_MEDIA_FILES)
            Toast.makeText(this, mediaFiles?.size.toString(), Toast.LENGTH_LONG).show()
            adapter.setMediaFiles(mediaFiles)
        }

    private val permissionRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (!result && PermissionUtils.isPermissionDeniedPermanently(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    this,
                    "Read External Storage Permission is Denied Permanently !!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                openSelectorLauncher.launch(Intent(this, MediaSelectorActivity::class.java))
            }
        }

}