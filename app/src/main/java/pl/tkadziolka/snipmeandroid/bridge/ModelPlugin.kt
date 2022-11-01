package pl.tkadziolka.snipmeandroid.bridge

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.KoinComponent

/*
 flutter pub run pigeon \
  --input bridge/main_model.dart \
  --dart_out lib/model/main_model.dart \
  --java_out ../app/src/main/java/pl/tkadziolka/snipmeandroid/bridge/Bridge.java \
  --java_package "pl.tkadziolka.snipmeandroid.bridge"
 */

abstract class ModelPlugin<T>: FlutterPlugin, KoinComponent {

    abstract fun onSetup(messenger: BinaryMessenger, bridge: T?)

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onSetup(binding.binaryMessenger, this as T)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onSetup(binding.binaryMessenger, null)
    }
}