package com.carelon.whe.ui.dashboard.dialog

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.carelon.whe.BR
import com.carelon.whe.R
import com.carelon.whe.base.BaseDialogFragment
import com.carelon.whe.constants.BundleKeyConstants
import com.carelon.whe.databinding.FragmentWelcomeDialogBinding
import com.carelon.whe.util.safeDismiss
import com.carelon.whe.util.setSafeOnClickListener
import com.carelon.whe.widget.custom_typeface.CustomTypeface
import com.carelon.whe.widget.span.span
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dialog fragment class to show welcome popup
 */
@AndroidEntryPoint
class WelcomeDialogFragment : BaseDialogFragment<FragmentWelcomeDialogBinding>() {

    private val welcomeDialogViewModel: WelcomeDialogViewModel by viewModels()

    override fun getContentView(): Int = R.layout.fragment_welcome_dialog

    override fun getBindingVariable(): Int = BR.welcomeDialogViewModel

    override fun getViewModel() = welcomeDialogViewModel

    private val userName: String by lazy {
        requireArguments().getString(BundleKeyConstants.EXTRA_USER_NAME, "")
    }

    private var binding: FragmentWelcomeDialogBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getDataBinding()
        welcomeDialogViewModel.setWelcomeUserName(userName)
        initOnClicks()
        initDescriptionUI()
        initBonusRewardsUI()
    }

    private fun initOnClicks() {
        binding?.btnLetsGo?.setSafeOnClickListener {
            safeDismiss(parentFragmentManager)
        }
    }

    /**
     * Function to span the description text
     */
    private fun initDescriptionUI() {
        val welcomeDescription = getString(R.string.str_welcome_description)
        val someSteps = getString(R.string.str_some_steps)
        val beforeDescString = welcomeDescription.substringBefore(someSteps)
        val afterDescString = welcomeDescription.substringAfter(someSteps)
        binding?.txtDescription?.text = span {
            +beforeDescString
            span {
                text = someSteps
                typeface = ResourcesCompat.getFont(requireContext(), R.font.sans_semibold)
            }
            +afterDescString
        }
    }

    /**
     * Function to span the bonus rewards text
     */
    private fun initBonusRewardsUI() {
        val list =
            arrayListOf(getString(R.string.str_first_steps), getString(R.string.str_bonus_rewards))
        binding?.imgBonus?.text =
            boldSpecificStrings(getString(R.string.str_bonus_rewards_text), list)
    }

    /**
     * Bold all the strings based on the list
     */
    private fun boldSpecificStrings(
        content: String,
        words: List<String>
    ): SpannableString {
        val formattedString = SpannableString(content)
        for (i in words) {
            formattedString.setSpan(
                CustomTypeface(ResourcesCompat.getFont(requireContext(), R.font.sans_semibold)!!),
                content.indexOf(i, 0, true),
                content.indexOf(i).plus(i.length),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return formattedString
    }

    companion object {
        fun newInstance(userName: String): WelcomeDialogFragment {
            val welcomeDialogFragment = WelcomeDialogFragment()
            val bundle = Bundle()
            bundle.putString(BundleKeyConstants.EXTRA_USER_NAME, userName)
            welcomeDialogFragment.arguments = bundle
            return welcomeDialogFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}