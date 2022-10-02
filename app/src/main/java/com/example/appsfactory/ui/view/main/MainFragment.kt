package com.example.appsfactory.ui.view.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appsfactory.data.local.entity.LocalAlbum
import com.example.appsfactory.databinding.FragmentMainBinding
import com.example.appsfactory.ui.adapter.TopAlbumsAdapter
import com.example.appsfactory.ui.base.BaseFragment
import com.example.appsfactory.util.gone
import com.example.appsfactory.util.inVisible
import com.example.appsfactory.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var topAlbumsAdapter: TopAlbumsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        topAlbumsAdapter = TopAlbumsAdapter(this::onAlbumClicked)
        binding.recyclerView.adapter = topAlbumsAdapter
    }

    private fun onAlbumClicked(album: LocalAlbum) {
        val name = album.name
        val artist = album.artist
        val image = album.image

        val action =
            MainFragmentDirections.actionMainFragmentToDetailFragment(name, artist, image, true)
        findNavController().navigate(action)
    }

    private fun setupObserver() {
        mainViewModel.getBookmarkedAlbums().observe(viewLifecycleOwner) { submitList(it) }
    }

    private fun submitList(localAlbums: List<LocalAlbum>) {
        if (localAlbums.isEmpty()) {
            binding.recyclerView.inVisible()
            binding.emptyView.emptyViewLayout.visible()
        } else {
            binding.recyclerView.visible()
            binding.emptyView.emptyViewLayout.gone()
            topAlbumsAdapter.setData(localAlbums)
        }
    }

}