package com.max.mediaselector.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.max.mediaselector.MediaFile
import com.max.mediaselector.MediaSelectorActivity
import com.max.mediaselector.R
import com.max.mediaselector.databinding.MediaSelectorFragmentMediaListBinding
import com.max.mediaselector.view.MediaSelectorRecyclerView


class MediaListFragment : Fragment() {

    private val binding: MediaSelectorFragmentMediaListBinding by lazy {
        MediaSelectorFragmentMediaListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).setSupportActionBar(binding.mediaSelectorToolBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mediaSelectorToolBar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        val maxSelectCount = 9

        binding.mediaSelectorRecyclerView.init(
            enableImage = true,
            enableVideo = true,
            enableSelect = true,
            maxSelectCount = 9
        )

        refreshSelectedCountText(0, maxSelectCount)
        binding.mediaSelectorRecyclerView.setOnSelectCountChangedListener(object :
            MediaSelectorRecyclerView.OnSelectMediaFileListener {

            override fun onSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    binding.mediaSelectorRecyclerView.getSelectedMediaFiles().size,
                    maxSelectCount
                )
            }

            override fun onUnSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    binding.mediaSelectorRecyclerView.getSelectedMediaFiles().size,
                    maxSelectCount
                )
            }
        })

        binding.mediaSelectorRecyclerView.setOnMediaItemClickListener(object :
            MediaSelectorRecyclerView.OnMediaItemClickListener {
            override fun onMediaItemClick(mediaFile: MediaFile) {

                val fragment: Fragment = when (mediaFile.mediaType) {
                    MediaFile.MediaType.IMAGE -> {
                        MediaImagePreviewFragment.getInstance(mediaFile)
                    }
                    MediaFile.MediaType.VIDEO -> {
                        MediaVideoPreviewFragment.getInstance(mediaFile)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.media_selector_fragment_enter_anim,
                        R.anim.media_selector_fragment_exit_anim,
                        R.anim.media_selector_fragment_enter_anim,
                        R.anim.media_selector_fragment_exit_anim
                    )
                    .add(R.id.media_selector_fragment_container, fragment)
                    .addToBackStack(fragment::class.java.name)
                    .commit()
            }
        })

        binding.mediaSelectorSelectConfirmButton.setOnClickListener {
            val outIntent = Intent()
            outIntent.putExtra(
                MediaSelectorActivity.SELECTED_MEDIA_FILES,
                binding.mediaSelectorRecyclerView.getSelectedMediaFiles()
            )
            activity?.setResult(AppCompatActivity.RESULT_OK, outIntent)
            activity?.finish()
        }
    }

    private fun refreshSelectedCountText(currentSelectCount: Int, maxSelectCount: Int) {
        binding.mediaSelectorSelectConfirmButton.text =
            getString(
                R.string.media_selector_confirm_button_pattern,
                currentSelectCount,
                maxSelectCount
            )
    }


}