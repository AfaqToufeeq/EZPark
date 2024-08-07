package com.admin.ezpark.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.ezpark.databinding.FragmentViewOwnerParkingBinding
import com.admin.ezpark.ui.adapters.ViewOwnerParkingAdapter
import com.admin.ezpark.ui.viewmodels.ParkingLotViewModel
import com.admin.ezpark.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewOwnerParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ViewOwnerParkingFragment : BaseFragment() {
    private var _binding: FragmentViewOwnerParkingBinding? = null
    private val binding: FragmentViewOwnerParkingBinding get() = _binding!!
    private val viewModel: ParkingLotViewModel by viewModels()
    private lateinit var viewOwnerParkAdapter: ViewOwnerParkingAdapter

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewOwnerParkingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        setRecyclerView()
        setObservers()
        styleSearchView()
    }

    private fun styleSearchView() {
        val searchView = binding.searchView
        // Remove the underline
        val searchPlate = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }


    private fun initViews() {
        binding.shimmerViewContainer.startShimmer()
    }

    private fun setObservers() {
        viewModel.ownerInfo.observe(viewLifecycleOwner) { (ownerList, flag, msg) ->
            if (flag) {
                with(binding) {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                viewOwnerParkAdapter.submitList(ownerList)
            } else {
                showToast(requireContext(), msg)
            }
        }
    }

    private fun setListeners() {
        binding.toolbar.backIV.setOnClickListener { setNavigationBack() }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Don't handle submit, we want real-time filtering
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewOwnerParkAdapter.filter.filter(newText)
                return true // Indicate we've handled the text change
            }
        })

    }

    private fun setNavigationBack() {
        findNavController().popBackStack()
    }

    private fun setRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            viewOwnerParkAdapter = ViewOwnerParkingAdapter {
                // Handle item click
            }
            adapter = viewOwnerParkAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewOwnerParkingFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewOwnerParkingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}