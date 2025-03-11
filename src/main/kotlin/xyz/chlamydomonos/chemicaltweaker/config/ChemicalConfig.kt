package xyz.chlamydomonos.chemicaltweaker.config

import kotlinx.serialization.Serializable

@Serializable
data class ChemicalConfig(
    val name: String,
    val type: String,
    val tint: String? = null,
    val hidden: Boolean? = null,
    val texture: String? = null,
    val oreTag: String? = null,
    val attributes: List<ChemicalAttributeConfig>? = null
)
