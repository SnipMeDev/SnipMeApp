package dev.snipme.snipmeapp.bridge

import dev.snipme.snipmeapp.channel.ChannelStateStreamHandler
import dev.snipme.snipmeapp.channel.ModelStateData
import dev.snipme.snipmeapp.channel.PigeonEventSink
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FlowChannelStateStreamHandler : ChannelStateStreamHandler() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val sinkFlow = MutableSharedFlow<ModelStateData>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun onSetup(messenger: BinaryMessenger) {
        println("StreamHandlerPlugin onSetup")
        register(messenger, this)
    }

    override fun onListen(p0: Any?, sink: PigeonEventSink<ModelStateData>) {
        scope.launch { sinkFlow
            .onEach { print("StreamHandlerPlugin onEach $it") }
            .collect { sink.success(it) } }
    }

    override fun onCancel(p0: Any?) {
        println("StreamHandlerPlugin onCancel")
    }

    fun zip(flow: Flow<ModelStateData>) {
        scope.launch {
            flow.map {
                println("StreamHandlerPlugin zip map $it")
                it
            }.collectLatest {
                println("StreamHandlerPlugin zip collectLatest $it")
                sinkFlow.emit(it)
            }
        }
    }
}

class StreamHandlerPlugin : FlutterPlugin, KoinComponent {
    private val stateStream by inject<FlowChannelStateStreamHandler>()

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        println("StreamHandlerPlugin onAttachedToEngine")
        stateStream.onSetup(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {}
}