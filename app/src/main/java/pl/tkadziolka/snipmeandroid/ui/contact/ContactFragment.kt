package pl.tkadziolka.snipmeandroid.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_contact.*
import org.koin.android.ext.android.inject
import pl.tkadziolka.snipmeandroid.R

class ContactFragment: Fragment() {

    private val navigator by inject<ContactNavigator>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_contact, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactToolbar.setupWithNavController(findNavController())

        contactFacebookAction.setOnClickListener { navigator.goToFacebook(requireContext()) }
        contactInstagramAction.setOnClickListener { navigator.goToInstagram(requireContext()) }
        contactGithubAction.setOnClickListener { navigator.goToGithub(requireContext()) }
        contactTwitterAction.setOnClickListener { navigator.goToTwitter(requireContext()) }
    }
}