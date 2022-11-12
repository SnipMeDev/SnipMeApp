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

    override fun getState(): Bridge.DetailModelStateData = getData(model.state.value)

    override fun onSetup(messenger: BinaryMessenger, bridge: Bridge.DetailModelBridge?) {
        Bridge.DetailModelBridge.setup(messenger, bridge)
    }

    private fun getData(viewState: DetailViewState) = Bridge.DetailModelStateData().apply {
        state = viewState.toModelState()
        is_loading = viewState is Loading
        data = (viewState as? Loaded)?.snippet?.toModelData()
    }

    private fun DetailViewState.toModelState() =
        when (this) {
            Loading -> Bridge.ModelState.LOADING
            is Loaded -> Bridge.ModelState.LOADED
            else -> Bridge.ModelState.ERROR
        }
}