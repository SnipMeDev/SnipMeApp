package pl.tkadziolka.snipmeandroid.ui.donate

import android.content.Context
import androidx.lifecycle.ViewModel
import pl.tkadziolka.snipmeandroid.domain.payment.PaymentOption

class DonateViewModel(private val navigator: DonateNavigator) : ViewModel() {
    private var selectedOption = PaymentOption.PAYPAL


    fun setPaymentOption(option: PaymentOption) {
        selectedOption = option
    }

    fun proceedToPayment(context: Context) {
        when (selectedOption) {
            PaymentOption.GOOGLE_PAY -> Unit
            PaymentOption.PAYPAL -> navigator.goToPayPal(context)
            PaymentOption.CREDIT_CARD -> navigator.goToCreditCard(context)
        }
    }
}