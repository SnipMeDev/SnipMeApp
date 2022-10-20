package pl.tkadziolka.snipmeandroid.ui.donate

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_donate.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.payment.PaymentOption
import pl.tkadziolka.snipmeandroid.ui.viewmodel.ViewModelFragment
import pl.tkadziolka.snipmeandroid.util.extension.setOnClick

class DonateFragment : ViewModelFragment<DonateViewModel>(DonateViewModel::class) {

    override val layout: Int = R.layout.fragment_donate

    override fun onViewCreated() {
        donateToolbar.setupWithNavController(findNavController())
        setupActions()
    }

    override fun observeViewModel() = Unit

    private fun setupActions() {
        donateOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.donateOptionGooglePay -> Unit
                R.id.donateOptionPayPal -> viewModel.setPaymentOption(PaymentOption.PAYPAL)
                R.id.donateOptionCreditCard -> viewModel.setPaymentOption(PaymentOption.CREDIT_CARD)
                else -> Unit
            }
        }

        donateAction.setOnClick {
            viewModel.proceedToPayment(requireContext())
        }
    }
}