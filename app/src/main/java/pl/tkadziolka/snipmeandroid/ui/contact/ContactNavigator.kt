package pl.tkadziolka.snipmeandroid.ui.contact

import android.content.Context
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.util.extension.safeOpenWebsite

class ContactNavigator {

    fun goToFacebook(context: Context) {
        context.safeOpenWebsite(BuildConfig.FACEBOOK_PAGE)
    }

    fun goToInstagram(context: Context) {
        context.safeOpenWebsite(BuildConfig.INSTAGRAM_PAGE)
    }

    fun goToGithub(context: Context) {
        context.safeOpenWebsite(BuildConfig.GITHUB_PAGE)
    }

    fun goToTwitter(context: Context) {
        context.safeOpenWebsite(BuildConfig.TWITTER_PAGE)
    }
}