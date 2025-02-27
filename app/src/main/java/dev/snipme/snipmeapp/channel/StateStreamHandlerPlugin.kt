package dev.snipme.snipmeapp.channel

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FlowChannelStateStreamHandler : ChannelStateStreamHandler() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var sink: PigeonEventSink<ModelStateData>? = null

    fun onSetup(messenger: BinaryMessenger) {
        register(messenger, this)
    }

    override fun onListen(p0: Any?, sink: PigeonEventSink<ModelStateData>) {
        this.sink = sink
    }

    override fun onCancel(p0: Any?) {
        sink = null
    }

    fun zip(flow: Flow<ModelStateData>) {
        flow.onEach { sink?.success(it) }.launchIn(scope)
    }
}

class StateStreamHandlerPlugin : FlutterPlugin, KoinComponent {
    private val stateStream by inject<FlowChannelStateStreamHandler>()

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        stateStream.onSetup(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {}
}