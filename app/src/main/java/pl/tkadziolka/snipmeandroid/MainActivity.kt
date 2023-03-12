package pl.tkadziolka.snipmeandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import pl.tkadziolka.snipmeandroid.bridge.detail.DetailModelPlugin
import pl.tkadziolka.snipmeandroid.bridge.login.LoginModelPlugin
import pl.tkadziolka.snipmeandroid.bridge.main.MainModelPlugin

class MainActivity : AppCompatActivity() {
    private lateinit var flutterEngine : FlutterEngine

    private val cachedEngineId = "ID_CACHED_FLUTTER_ENGINE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         FlutterEngine(this).apply {
             dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())

             plugins.add(LoginModelPlugin())
             plugins.add(MainModelPlugin())
             plugins.add(DetailModelPlugin())

             FlutterEngineCache.getInstance().put(cachedEngineId, this)

             startActivity(FlutterActivity.withCachedEngine(cachedEngineId).build(baseContext))
         }
    }
}