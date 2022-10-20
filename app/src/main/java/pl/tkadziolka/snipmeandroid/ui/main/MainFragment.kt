package pl.tkadziolka.snipmeandroid.ui.main

import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SNIPPET_PAGE_SIZE
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.domain.user.User
import pl.tkadziolka.snipmeandroid.ui.viewmodel.ViewModelFragment
import pl.tkadziolka.snipmeandroid.ui.viewmodel.observeNotNull
import pl.tkadziolka.snipmeandroid.util.extension.*
import pl.tkadziolka.snipmeandroid.util.view.LoadMoreListener

private const val TOP_OF_LIST = 0

class MainFragment : ViewModelFragment<MainViewModel>(MainViewModel::class) {

    private val loadMore = LoadMoreListener(SNIPPET_PAGE_SIZE) {
        viewModel.loadNextPage()
    }

    private val mainAdapter by lazy {
        SnippetAdapter(
            clickListener = { snippet ->
                viewModel.goToDetail(findNavController(), snippet.uuid)
            },
            pressListener = { snippet ->
                with(snippet) {
                    viewModel.goToPreview(findNavController(), title, uuid, code.raw, language.raw)
                }
            }
        )
    }

    @LayoutRes
    override val layout: Int = R.layout.fragment_main

    override fun onViewCreated() {
        if (BuildConfig.DEBUG)
            mainVersion.text = "v${BuildConfig.VERSION_NAME}"

        setupMenu()
        setupActions()
        showLogin("")
        mainList.adapter = mainAdapter
        viewModel.init()
        viewModel.refreshSnippetUpdates()
    }

    override fun observeViewModel() {
        viewModel.state.observeNotNull(this) { state ->
            when (state) {
                Loading -> showLoading()
                is Loaded -> showData(state.user, state.snippets)
                is Error -> viewModel.goToError(findNavController(), state.message)
            }
        }

        viewModel.event.observeNotNull(this) { event ->
            when (event) {
                is ListRefreshed -> {
                    mainList.smoothScrollToPosition(TOP_OF_LIST)
                    mainAppBar.setExpanded(true)
                }
                is Alert -> showToast(event.message)
                is Logout -> viewModel.goToLogin(findNavController())
            }
        }
    }

    private fun showLoading() {
        mainLoading.visible()
    }

    private fun showData(user: User, snippets: List<Snippet>) {
        mainLoading.gone()
        showLogin(user.login)
        mainImage.loadWithFallback(user.photo)
        with(mainList) {
            visible()
            mainAdapter.submitList(snippets)
        }
    }

    private fun showLogin(login: String) {
        mainLogin.text = login
    }

    private fun setupActions() {
        mainAdd.setOnClick { viewModel.goToEdit(findNavController()) }
        mainFilters.filterListener = { viewModel.filter(it) }
        mainList.addOnScrollListener(loadMore)
    }

    private fun setupMenu() {
        with(mainToolbar) {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.mainMenuDonate -> { viewModel.goToDonate(findNavController()); true }
                    R.id.mainMenuContact -> { viewModel.goToContact(findNavController()); true }
                    R.id.mainMenuLogout -> { viewModel.logOut(); true }
                    else -> false
                }
            }
        }
    }
}