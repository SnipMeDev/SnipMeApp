package pl.tkadziolka.snipmeandroid.bridge.detail

import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import pl.tkadziolka.snipmeandroid.bridge.Bridge
import pl.tkadziolka.snipmeandroid.bridge.ModelPlugin
import pl.tkadziolka.snipmeandroid.bridge.toModelData
import pl.tkadziolka.snipmeandroid.ui.detail.DetailViewState
import pl.tkadziolka.snipmeandroid.ui.detail.Loaded
import pl.tkadziolka.snipmeandroid.ui.detail.Loading

class DetailModelPlugin: ModelPlugin<Bridge.DetailModelBridge>(), Bridge.DetailModelBridge {
    private val model: DetailModel by inject()
    private var oldState: DetailViewState? = null

    override fun getState(): Bridge.DetailModelStateData = getData(model.state.value)

    override fun onSetup(messenger: BinaryMessenger, bridge: Bridge.DetailModelBridge?) {
        Bridge.DetailModelBridge.setup(messenger, bridge)
    }

    override fun load(uuid: String) {
        model.load(uuid)
    }

    override fun like() {
        model.like()
    }

    override fun dislike() {
        model.dislike()
    }

    override fun save() {
        model.save()
    }

    override fun copyToClipboard() {
        model.copyToClipboard()
    }

    override fun share() {
        TODO("Not yet implemented")
    }

    private fun getData(viewState: DetailViewState): Bridge.DetailModelStateData {
        oldState = viewState
        return Bridge.DetailModelStateData().apply {
            state = viewState.toModelState()
            is_loading = viewState is Loading
            data = (viewState as? Loaded)?.snippet?.toModelData()
            oldHash = oldState?.hashCode()?.toLong()
            newHash = viewState.hashCode().toLong()
        }
    }

    private fun DetailViewState.toModelState() =
        when (this) {
            Loading -> Bridge.ModelState.LOADING
            is Loaded -> Bridge.ModelState.LOADED
            else -> Bridge.ModelState.ERROR
        }
}