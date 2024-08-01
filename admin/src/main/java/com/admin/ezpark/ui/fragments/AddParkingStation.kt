package com.admin.ezpark.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.admin.ezpark.R
import com.admin.ezpark.databinding.FragmentAddParkingStationBinding
import com.admin.ezpark.ui.adapters.ViewPagerAdapter
import com.admin.ezpark.ui.viewmodels.ParkingLotViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class AddParkingStation : Fragment() {
    private var _binding: FragmentAddParkingStationBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: ParkingLotViewModel by viewModels()

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
        _binding = FragmentAddParkingStationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabViewPager()
    }

    private fun setUpTabViewPager() {
        val tabTitles = listOf(
            getString(R.string.tab_title_one),
            getString(R.string.tab_title_two)
        )

        with(binding) {
            viewPager.adapter = ViewPagerAdapter(requireActivity())

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}