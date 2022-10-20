package pl.tkadziolka.snipmeandroid.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import pl.tkadziolka.snipmeandroid.Messages
import pl.tkadziolka.snipmeandroid.PigeonPlugin
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.snippets.GetSnippetsUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetScope
import timber.log.Timber

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

        flutterEngine.plugins.add(PigeonPlugin())

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