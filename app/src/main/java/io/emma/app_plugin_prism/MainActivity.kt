package io.emma.app_plugin_prism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import io.emma.android.model.EMMANativeAd
import io.emma.plugin_prism.EMMAInAppPrismPlugin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.showPrism).setOnClickListener {
            val plugin = EMMAInAppPrismPlugin()
            plugin.show(this, EMMANativeAd())
        }
    }
}