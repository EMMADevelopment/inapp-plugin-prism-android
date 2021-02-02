package io.emma.app_plugin_prism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.emma.android.model.EMMANativeAd
import io.emma.plugin_prism.EMMAInappPlugin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val plugin = EMMAInappPlugin()
        plugin.show(this, EMMANativeAd())
    }
}