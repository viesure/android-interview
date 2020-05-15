package io.viesure.hiring.screen.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

abstract class BaseFragment : Fragment() {
    protected val kodein by kodein()
    private val navigator: Navigator by lazy { kodein.direct.instance<Navigator>() }
    protected abstract val viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()
        viewModel.navigation.observe(viewLifecycleOwner, Observer { it.getContentIfNotHandled()?.let { navigator.open(requireActivity(), it) } })
    }
}