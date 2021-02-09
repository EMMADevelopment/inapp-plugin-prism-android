package io.emma.plugin_prism

import java.io.Serializable

data class EMMAPrism(val campaignId: Int, val openInApp: Boolean, val sides: List<EMMAPrismSide>): Serializable