package com.admin.ezpark.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.admin.ezpark.R
import com.admin.ezpark.data.models.DashboardCard
import com.admin.ezpark.databinding.FragmentHomeBinding
import com.admin.ezpark.enums.DashboardFields
import com.admin.ezpark.sealedclass.DashboardNavigationAction
import com.admin.ezpark.ui.adapters.DashboardAdapter
import com.admin.ezpark.ui.viewmodels.DashboardViewModel
import com.admin.ezpark.utils.Utils.showToast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dashboardAdapter: DashboardAdapter
    private val viewModel: DashboardViewModel by viewModels()


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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObserver()
    }

    private fun setRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireActivity(), 1)
            dashboardAdapter = DashboardAdapter {  dashboardCard ->
                navigationHandling(dashboardCard)
            }
            adapter = dashboardAdapter
        }

    }

    private fun navigationHandling(dashboardCard: DashboardCard) {
        val action = when (dashboardCard.title) {
            DashboardFields.AddOwner -> DashboardNavigationAction.NavigateToAddOwner
            DashboardFields.ViewOwner -> DashboardNavigationAction.NavigateToViewOwner
            else -> {}
        }

        when (action) {
            DashboardNavigationAction.NavigateToAddOwner -> findNavController().navigate(R.id.action_homeFragment_to_parkingOwnerInfoFragment)
            else -> {}
        }
    }

    private fun setObserver() {
        viewModel.dashboardCardsType2.observe(viewLifecycleOwner) { cards ->
            dashboardAdapter.submitList(cards)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}