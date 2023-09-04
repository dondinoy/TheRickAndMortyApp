package learn.daniel.rickAndMortyNew.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import learn.daniel.rickAndMortyNew.adapters.CharactersAdapter
import learn.daniel.rickAndMortyNew.viewModel.SearchListViewModel
import learn.daniel.rickandmortynew.databinding.FragmentSearchListBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchListFragment : Fragment() {

    private var _binding: FragmentSearchListBinding? = null

    private val binding get() = _binding!!
    private var searchViewModel : SearchListViewModel ?= null
    private val adapter = CharactersAdapter()
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel = ViewModelProvider(this).get(SearchListViewModel::class.java)

        _binding = FragmentSearchListBinding.inflate(inflater , container , false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            layoutManager = GridLayoutManager(this@SearchListFragment.context, 2)
            adapter = this@SearchListFragment.adapter
        }
        searchViewModel?.apply {
            initCharacters()
            characters.observe(viewLifecycleOwner) {
                adapter.characters = it
                adapter.notifyDataSetChanged()
            }
        }
        binding.searchHolder.setOnQueryTextListener( object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel?.onSearch(newText)
                return true
            }
        } )

        binding.list.addOnScrollListener( object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastPosition >= 0 && lastPosition + 10 > adapter.itemCount && binding.searchHolder.query.isNullOrEmpty()) {
                    searchViewModel?.loadMore()
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}