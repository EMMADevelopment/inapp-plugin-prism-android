package io.emma.plugin_prism

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EMMAInappPrismActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emma_prism)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.layout_main, EMMAInappPrismFragment.newInstance(null));
        ft.commit();
    }
}