package pl.tkadziolka.snipmeandroid.ui.detail

import android.os.Build
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_detail.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.ui.viewmodel.ViewModelFragment
import pl.tkadziolka.snipmeandroid.ui.viewmodel.observeNotNull
import pl.tkadziolka.snipmeandroid.util.extension.*

class DetailFragment : ViewModelFragment<DetailViewModel>(DetailViewModel::class) {
    private val args: DetailFragmentArgs by navArgs()

    override val layout: Int = R.layout.fragment_detail

    override fun onViewCreated() {
        setupToolbar()
        setupActions()
        viewModel.load(args.uuid)
    }

    override fun observeViewModel() {
        viewModel.state.observeNotNull(this) { state ->
            when (state) {
                is Loading -> showLoading()
                is Loaded -> showData(state.snippet)
                is Error -> viewModel.goToError(findNavController(), state.error)
            }
        }

        viewModel.event.observeNotNull(this) { event ->
            when (event) {
                is Alert -> showToast(event.message)
                is Logout -> {
                    viewModel.goToLogin(findNavController())
                }
            }
        }
    }

    private fun showLoading() {
        detailLoading.visible()
    }

    private fun showData(snippet: Snippet) {
        detailLoading.gone()
        with(snippet) {
            showActionsForOwner(isOwner)
            detailName.text = title
            detailLanguage.text = language.raw
            detailLikeCounter.text = numberOfLikes.toString()
            detailDislikeCounter.text = numberOfDislikes.toString()
            setReactionState(snippet.userReaction)
            detailCode.setCodeWithTheme(code.raw, language.type.fileExtension)
        }
    }

    private fun showActionsForOwner(isOwner: Boolean) {
        detailToolbar.menu.findItem(R.id.detailMenuShare).isVisible = isOwner
        detailEdit.isVisible = isOwner
    }

    private fun setupActions() {
        detailEdit.setOnClick {
            viewModel.goToEdit(findNavController(), args.uuid)
        }

        detailLikeAction.setOnClick {
            setReactionAvailability(isAvailable = false)
            viewModel.like()
        }

        detailDislikeAction.setOnClick {
            setReactionAvailability(isAvailable = false)
            viewModel.dislike()
        }
    }

    private fun setupToolbar() {
        with(detailToolbar) {
            setupWithNavController(findNavController())
            inflateMenu(R.menu.detail_menu)
            setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.detailMenuCopy -> {
                        viewModel.copyToClipboard()
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                            // Android 12+ shows message itself
                            showToast(getString(R.string.message_copied, detailName.text))
                        }
                        true
                    }
                    R.id.detailMenuShare -> {
                        viewModel.goToShare(findNavController(), args.uuid); true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setReactionState(userReaction: UserReaction) {
        setReactionAvailability(isAvailable = true)
        when (userReaction) {
            UserReaction.LIKE -> {
                detailLikeAction.tint(R.color.highlight)
                detailDislikeAction.tint(R.color.white)
            }
            UserReaction.DISLIKE -> {
                detailLikeAction.tint(R.color.white)
                detailDislikeAction.tint(R.color.highlight)
            }
            UserReaction.NONE -> {
                detailLikeAction.tint(R.color.white)
                detailDislikeAction.tint(R.color.white)
            }
        }
    }

    private fun setReactionAvailability(isAvailable: Boolean) {
        detailLikeAction.isEnabled = isAvailable
        detailLikeAction.isEnabled = isAvailable
    }
}