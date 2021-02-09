package io.emma.plugin_prism

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity

class EMMAInAppPrismActivity: FragmentActivity()  {

    private var dialogFragment : EMMAPrismDialogFragment? = null

    companion object {
        private const val PRISM = "prism"

        fun makeIntent(context: Activity, prism: EMMAPrism): Intent {
            val intent = Intent(context, EMMAInAppPrismActivity::class.java)
            intent.putExtra(PRISM, prism)
            return intent;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emma_prism)

        val prism = intent.getSerializableExtra(PRISM) as? EMMAPrism
        if (prism != null && prism.sides.isNotEmpty()) {
            dialogFragment = EMMAPrismDialogFragment().apply {
                addPrism(prism)
                isCancelable = false
            }

            dialogFragment?.show(supportFragmentManager, EMMAPrismDialogFragment.TAG)
        }
    }
}