package pl.tkadziolka.snipmeandroid.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import pl.tkadziolka.snipmeandroid.bridge.main.MainModelPlugin
import pl.tkadziolka.snipmeandroid.R

class MainActivity : AppCompatActivity() {
    private lateinit var flutterEngine : FlutterEngine

    private val cachedEngineId = "ENGINE_1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(this)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        flutterEngine.plugins.add(MainModelPlugin())

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put(cachedEngineId, flutterEngine)

        startActivity(
            FlutterActivity
                .withCachedEngine(cachedEngineId)
                .build(this)
        )
    }
}