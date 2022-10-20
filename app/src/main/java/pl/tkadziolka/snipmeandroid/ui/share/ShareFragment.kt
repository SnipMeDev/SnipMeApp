package pl.tkadziolka.snipmeandroid.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_share.*
import org.koin.android.ext.android.inject
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.share.ShareUser
import pl.tkadziolka.snipmeandroid.ui.viewmodel.observeNotNull
import pl.tkadziolka.snipmeandroid.util.extension.*

class ShareFragment : DialogFragment() {

    private val viewModel: ShareViewModel by inject()

    private val args: ShareFragmentArgs by navArgs()

    private val adapter by lazy {
        ShareUserAdapter { shareUser ->
            viewModel.share(args.uuid, shareUser.user.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_share, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showTransparentBackground()
        showFullscreen()

        shareContainer.setOnOutsideClick(R.dimen.spacing_medium) {
            dismiss()
        }

        shareList.adapter = adapter
        shareName.addTextChangedListener { editable ->
            viewModel.search(editable.toString(), args.uuid)
        }

        shareCloseAction.setOnClick {
            dismiss()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observeNotNull(this) { state ->
            hideLoading()
            when (state) {
                is Loading -> showLoading()
                is Loaded -> showData(state.users)
                is Shared -> {
                    showToast(getString(R.string.message_shared, state.shareUser.user.login))
                    dismiss()
                }
                is Error -> {
                    showToast(state.message ?: getString(R.string.error_message_generic))
                    dismiss()
                }
            }
        }

        viewModel.event.observeNotNull(this) { event ->
            when (event) {
                is Alert -> showToast(event.message)
                is Logout -> viewModel.goToLogin(findNavController())
            }
        }
    }

    private fun showLoading() {
        shareLoading.visible()
        shareHelperImage.gone()
        shareHelper.gone()
    }

    private fun hideLoading() {
        shareLoading.gone()
    }

    private fun showData(users: List<ShareUser>?) {
        if (users == null) {
            shareHelperImage.visible()
            shareHelper.visible()
            adapter.submitList(null)
            return
        }

        shareHelperImage.gone()
        shareHelper.gone()
        adapter.submitList(users)
    }
}