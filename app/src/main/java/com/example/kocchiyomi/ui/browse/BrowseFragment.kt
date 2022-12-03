package com.example.kocchiyomi.ui.browse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kocchiyomi.KocchiyomiApplication
import com.example.kocchiyomi.adapters.MangaListAdapter
import com.example.kocchiyomi.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {
    private lateinit var binding: FragmentBrowseBinding

    private val viewModel: BrowseViewModel by activityViewModels {
        BrowseViewModelFactory(
            (activity?.application as KocchiyomiApplication).database.mangaDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getFeed()
        binding = FragmentBrowseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        binding.browseRecyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        binding.browseRecyclerView.setHasFixedSize(true)

        val adapter = MangaListAdapter()
//        adapter.libraryIds = viewModel.getLibrary().map { it.id }

        adapter.onClick = {

        }

        binding.browseRecyclerView.adapter = adapter

        viewModel.feedResponse.observe(viewLifecycleOwner){
            response -> (binding.browseRecyclerView.adapter as MangaListAdapter).mangaList = response.mangaList
        }

        super.onViewCreated(view, savedInstanceState)
    }

}