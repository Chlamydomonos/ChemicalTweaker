package xyz.chlamydomonos.chemicaltweaker.config

import kotlinx.serialization.Serializable

@Serializable
data class ChemicalAttributeConfig(
    val type: String,
    val burnTicks: Int? = null,
    val energyDensity: String? = null,
    val radioactivity: Double? = null,
    val cooledGas: String? = null,
    val heatedGas: String? = null,
    val thermalEnthalpy: Double? = null,
    val conductivity: Double? = null,
)