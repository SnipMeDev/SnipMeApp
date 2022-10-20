package pl.tkadziolka.snipmeandroid.ui.donate

import android.content.Context
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.util.extension.safeOpenWebsite

class DonateNavigator {

    fun goToPayPal(context: Context) {
        context.safeOpenWebsite(BuildConfig.PAYPAL_PAGE)
    }

    fun goToCreditCard(context: Context) {
        context.safeOpenWebsite(BuildConfig.CARD_PAGE)
    }
}