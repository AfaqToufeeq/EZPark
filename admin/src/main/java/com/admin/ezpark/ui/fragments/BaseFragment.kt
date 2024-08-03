package com.admin.ezpark.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.admin.ezpark.R


open class BaseFragment : Fragment() {

    protected open val shouldUpdateConstraints = true

    private var constraintLayout: ConstraintLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        constraintLayout = requireActivity().findViewById(R.id.constraintLayout)

        constraintLayout?.let { updateNavFragmentConstraint() }
    }

    private fun updateNavFragmentConstraint() {
        constraintLayout?.let {
            with(ConstraintSet()) {
                clone(it)
                if (shouldUpdateConstraints) {
                    connect(R.id.nav_host_fragment, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                } else {
                    connect(R.id.nav_host_fragment, ConstraintSet.TOP, R.id.firstCard_RecyclerView, ConstraintSet.BOTTOM)
                }
                applyTo(it)
            }
        }
    }
}