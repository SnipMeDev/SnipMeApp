package dev.snipme.snipmeapp.bridge

import dev.snipme.snipmeapp.bridge.main.MainViewState
import dev.snipme.snipmeapp.channel.ChannelStateStreamHandler
import dev.snipme.snipmeapp.channel.ModelStateData
import dev.snipme.snipmeapp.channel.PigeonEventSink
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FlowChannelStateStreamHandler : ChannelStateStreamHandler() {
    private val state = MutableStateFlow<ModelStateData>()

    fun onSetup(messenger: BinaryMessenger) {
        register(messenger, this)
    }

    override fun onListen(p0: Any?, sink: PigeonEventSink<ModelStateData>) {

    }

    override fun onCancel(p0: Any?) {

    }

    fun zip(state: StateFlow<MainViewState>) {
        TODO("Not yet implemented")
    }
}

class StreamHandlerPlugin : FlutterPlugin, KoinComponent {
    private val stateStream by inject<FlowChannelStateStreamHandler>()
    

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        stateStream.onSetup(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
    }
}