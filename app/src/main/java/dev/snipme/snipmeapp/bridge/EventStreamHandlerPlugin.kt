package dev.snipme.snipmeapp.bridge

import dev.snipme.snipmeapp.channel.ChannelEventStreamHandler
import dev.snipme.snipmeapp.channel.ModelEventData
import dev.snipme.snipmeapp.channel.PigeonEventSink
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FlowChannelEventStreamHandler : ChannelEventStreamHandler() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val sinkFlow = MutableSharedFlow<ModelEventData>()

    fun onSetup(messenger: BinaryMessenger) {
        register(messenger, this)
    }

    override fun onListen(p0: Any?, sink: PigeonEventSink<ModelEventData>) {
        sinkFlow.onEach { sink.success(it) }.launchIn(scope)
    }

    override fun onCancel(p0: Any?) {}

    fun zip(flow: Flow<ModelEventData>) {
        flow.onEach { sinkFlow.emit(it) }.launchIn(scope)
    }
}

class EventStreamHandlerPlugin : FlutterPlugin, KoinComponent {
    private val eventStream by inject<FlowChannelEventStreamHandler>()

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        eventStream.onSetup(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {}
}