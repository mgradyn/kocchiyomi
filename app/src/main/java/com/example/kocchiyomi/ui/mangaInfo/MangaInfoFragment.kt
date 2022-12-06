package com.example.kocchiyomi.ui.mangaInfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.kocchiyomi.KocchiyomiApplication
import com.example.kocchiyomi.R
import com.example.kocchiyomi.adapters.ChapterListAdapter
import com.example.kocchiyomi.data.model.Manga
import com.example.kocchiyomi.databinding.FragmentMangaInfoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MangaInfoFragment : Fragment() {
    private lateinit var manga: Manga
    private lateinit var binding: FragmentMangaInfoBinding

    private val viewModel: MangaInfoViewModel by activityViewModels{
        MangaInfoViewModelFactory(
            (activity?.application as KocchiyomiApplication).database.mangaDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { manga = it.getSerializable("manga") as Manga }
        viewModel.getChapters(manga.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMangaInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnAddToLibrary.setOnClickListener {
//            viewModel.saveToLibrary(manga.id)
            binding.btnAddToLibrary.visibility = View.GONE
            binding.tvAddToLibrary.visibility = View.GONE
            binding.btnRemoveFromLibrary.visibility = View.VISIBLE
            binding.tvRemoveFromLibrary.visibility = View.VISIBLE
        }

        binding.btnRemoveFromLibrary.setOnClickListener {
//            viewModel.delete(manga.id)
            binding.btnRemoveFromLibrary.visibility = View.GONE
            binding.tvRemoveFromLibrary.visibility = View.GONE
            binding.btnAddToLibrary.visibility = View.VISIBLE
            binding.tvAddToLibrary.visibility = View.VISIBLE
        }

//        if ( viewModel.getById(manga.id)?.id == manga.id) {
//            binding.btnRemoveFromLibrary.visibility = View.VISIBLE
//            binding.btnAddToLibrary.visibility = View.GONE
//        }

        val adapter = ChapterListAdapter()
        adapter.onClick = {
//            val bundle = bundleOf( Pair("chapter_id", it.id) )
//            Navigation.findNavController(view).navigate(R.id.action_mangaInfoFragment_to_readerFragment, bundle)
        }
        binding.rvChapterList.adapter = adapter

        viewModel.chapters.observe(viewLifecycleOwner) { list ->
            (binding.rvChapterList.adapter as ChapterListAdapter).chapterList = list
        }

        binding.rvChapterList.layoutManager = LinearLayoutManager(requireContext())

        binding.tvMangaNameText.text = manga.attributes.title.en
        binding.tvMangaAuthorText.text = manga.relationships.first{ rel -> rel.type == "author"}.attributes?.name.toString()
        Log.d("attr", manga.relationships.first{ rel -> rel.type == "author"}.attributes?.name.toString())
        binding.tvMangaDescriptionText.text = manga.attributes.description.en

        binding.ivMangaCoverImage
            .load("https://uploads.mangadex.org/covers/${manga.id}/${manga.relationships.first{ rel -> rel.type == "cover_art" }.attributes?.fileName}.256.jpg")

        binding.ivMangaCoverImage.clipToOutline=true
    }


    override fun onResume() {
        (activity?.findViewById<BottomNavigationView>(R.id.bottom_nav))?.visibility = View.GONE
        super.onResume()
    }

}