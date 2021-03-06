package com.example.mvvmhomework.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mvvmhomework.R
import com.example.mvvmhomework.adapter.TaskAdapter
import com.example.mvvmhomework.databinding.FragmentHomeBinding
import com.example.mvvmhomework.viewmodel.HomeViewModel
import com.example.mvvmhomework.viewmodel.factory.HomeViewModelFactory

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.homeFragment = this
        binding.homeToolbarTitle = "Goals"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarHome)

        viewModel.taskList.observe(viewLifecycleOwner, {
            adapter = TaskAdapter(requireContext(), it, viewModel)
            binding.taskAdapter = adapter
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val tempViewModel: HomeViewModel by viewModels() {
            HomeViewModelFactory(requireActivity().application)
        }
        viewModel = tempViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.search(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.search(newText)
        return true
    }

    fun buttonClickFab(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addFragment)
    }

    override fun onResume() {
        super.onResume()
        viewModel.allTasks()
    }
}