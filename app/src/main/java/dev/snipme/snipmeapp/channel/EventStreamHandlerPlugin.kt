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

class FlowChannelEventStreamHandler : ChannelEventStreamHandler() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var sink: PigeonEventSink<ModelEventData>? = null

    fun onSetup(messenger: BinaryMessenger) {
        register(messenger, this)
    }

    override fun onListen(p0: Any?, sink: PigeonEventSink<ModelEventData>) {
        this.sink = sink
    }

    override fun onCancel(p0: Any?) {
        sink = null
    }

    fun zip(flow: Flow<ModelEventData>) {
        flow.onEach { sink?.success(it) }.launchIn(scope)
    }
}

class EventStreamHandlerPlugin : FlutterPlugin, KoinComponent {
    private val eventStream by inject<FlowChannelEventStreamHandler>()

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        eventStream.onSetup(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {}
}