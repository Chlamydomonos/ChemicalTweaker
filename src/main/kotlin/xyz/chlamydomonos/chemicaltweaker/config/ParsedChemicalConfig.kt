package xyz.chlamydomonos.chemicaltweaker.config

import mekanism.api.chemical.ChemicalBuilder

data class ParsedChemicalConfig(
    val name: String,
    val builder: ChemicalBuilder<*, *>
)
