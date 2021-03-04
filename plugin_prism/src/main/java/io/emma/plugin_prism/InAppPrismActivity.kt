package io.emma.plugin_prism

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.emma.android.plugins.EMMAInAppPlugin
import io.emma.android.utils.EMMALog

internal class InAppPrismActivity: FragmentActivity()  {

    private var dialogFragment : PrismDialogFragment? = null
    private var prism: Prism? = null

    companion object {
        private const val PRISM = "prism"

        fun makeIntent(context: Activity, prism: Prism): Intent {
            val intent = Intent(context, InAppPrismActivity::class.java)
            intent.putExtra(PRISM, prism)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emma_prism)

        val prism = intent.getSerializableExtra(PRISM) as? Prism
        if (prism != null && prism.sides.isNotEmpty()) {
            dialogFragment = PrismDialogFragment().apply {
                addPrism(prism)
                isCancelable = prism.canClose
            }
            dialogFragment?.show(supportFragmentManager, PrismDialogFragment.TAG)
        } else {
            EMMALog.w("Invalid prism or empty content. Skipping show")
            finish()
        }
    }

    override fun onBackPressed() {
        prism?.let {
            if (it.canClose) {
                EMMAInAppPlugin.invokeCloseListener(it.campaign)
                dialogFragment?.dismissAllowingStateLoss()
            }
        } ?:  dialogFragment?.dismissAllowingStateLoss()
        super.onBackPressed()
    }
}