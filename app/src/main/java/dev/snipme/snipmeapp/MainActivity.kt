package dev.snipme.snipmeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.snipme.snipmeapp.bridge.EventStreamHandlerPlugin
import dev.snipme.snipmeapp.bridge.StateStreamHandlerPlugin
import dev.snipme.snipmeapp.bridge.Details.DetailsModelPlugin
import dev.snipme.snipmeapp.bridge.login.LoginModelPlugin
import dev.snipme.snipmeapp.bridge.main.MainModelPlugin
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MainActivity : AppCompatActivity() {
    // TODO Improve flutter enginge management or remove
    private lateinit var flutterEngine: FlutterEngine

    private val cachedEngineId = "ID_CACHED_FLUTTER_ENGINE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FlutterEngine(this).apply {
            dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())

            plugins.add(
                setOf(
                    StateStreamHandlerPlugin(),
                    EventStreamHandlerPlugin(),
                    LoginModelPlugin(),
                    MainModelPlugin(),
                    DetailsModelPlugin()
                )
            )
            FlutterEngineCache.getInstance().put(cachedEngineId, this)

            startActivity(
                FlutterActivity.withCachedEngine(cachedEngineId)
                    .build(baseContext)
            )
        }
    }
}