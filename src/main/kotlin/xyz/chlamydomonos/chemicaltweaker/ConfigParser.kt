package xyz.chlamydomonos.chemicaltweaker

import kotlinx.serialization.json.Json
import mekanism.api.MekanismAPI
import mekanism.api.chemical.attribute.ChemicalAttribute
import mekanism.api.chemical.gas.GasBuilder
import mekanism.api.chemical.gas.attribute.GasAttributes.*
import mekanism.api.chemical.infuse.InfuseTypeBuilder
import mekanism.api.chemical.pigment.PigmentBuilder
import mekanism.api.chemical.slurry.SlurryBuilder
import mekanism.api.math.FloatingLong
import net.minecraft.resources.ResourceLocation
import xyz.chlamydomonos.chemicaltweaker.config.ChemicalAttributeConfig
import xyz.chlamydomonos.chemicaltweaker.config.ChemicalConfig
import xyz.chlamydomonos.chemicaltweaker.config.ParsedChemicalConfig

object ConfigParser {
    private fun parseChemicalAttribute(raw: ChemicalAttributeConfig): ChemicalAttribute = when (raw.type) {
        "fuel" -> {
            if (raw.burnTicks == null || raw.energyDensity == null) {
                throw RuntimeException("Invalid fuel")
            }
            Fuel(raw.burnTicks, FloatingLong.parseFloatingLong(raw.energyDensity))
        }

        "radiation" -> {
            if (raw.radioactivity == null) {
                throw RuntimeException("Invalid radiation")
            }
            Radiation(raw.radioactivity)
        }

        "cooled_coolant" -> {
            if (raw.heatedGas == null || raw.thermalEnthalpy == null || raw.conductivity == null) {
                throw RuntimeException("Invalid cooled coolant")
            }

            val location = ResourceLocation.tryParse(raw.heatedGas) ?: throw RuntimeException("Invalid cooled coolant")

            CooledCoolant({ MekanismAPI.gasRegistry().getValue(location)!! }, raw.thermalEnthalpy, raw.conductivity)
        }

        "heated_coolant" -> {
            if (raw.cooledGas == null || raw.thermalEnthalpy == null || raw.conductivity == null) {
                throw RuntimeException("Invalid heated coolant")
            }
            val location = ResourceLocation.tryParse(raw.cooledGas) ?: throw RuntimeException("Invalid heated coolant")

            HeatedCoolant({ MekanismAPI.gasRegistry().getValue(location)!! }, raw.thermalEnthalpy, raw.conductivity)
        }

        else -> throw RuntimeException("Invalid chemical attribute type: ${raw.type}")
    }

    private fun parseChemical(raw: ChemicalConfig): ParsedChemicalConfig {
        var builder = when (raw.type) {
            "gas" -> if (raw.texture != null) {
                val location = ResourceLocation.tryParse(raw.texture) ?: throw RuntimeException("Invalid texture")
                GasBuilder.builder(location)
            } else GasBuilder.builder()

            "slurry" -> {
                var builder = when (raw.texture) {
                    "clean" -> SlurryBuilder.clean()
                    "dirty" -> SlurryBuilder.dirty()
                    else -> {
                        if (raw.texture != null) {
                            val location = ResourceLocation.tryParse(raw.texture) ?: throw RuntimeException("Invalid texture")
                            SlurryBuilder.builder(location)
                        } else throw RuntimeException("Invalid texture")
                    }
                }
                if (raw.oreTag != null) {
                    if (!raw.oreTag.startsWith('#')) throw RuntimeException("Invalid ore tag")
                    val location = ResourceLocation.tryParse(raw.oreTag.substring(1)) ?: throw RuntimeException("Invalid ore tag")
                    builder = builder.ore(location)
                }
                builder
            }

            "pigment" -> if (raw.texture != null) {
                val location = ResourceLocation.tryParse(raw.texture) ?: throw RuntimeException("Invalid texture")
                PigmentBuilder.builder(location)
            } else PigmentBuilder.builder()

            "infuse_type" -> if (raw.texture != null) {
                val location = ResourceLocation.tryParse(raw.texture) ?: throw RuntimeException("Invalid texture")
                InfuseTypeBuilder.builder(location)
            } else InfuseTypeBuilder.builder()

            else -> throw RuntimeException("Invalid chemical type: ${raw.type}")
        }

        if (raw.hidden != null && raw.hidden) builder = builder.hidden()
        if (raw.tint != null) {
            if (raw.tint.startsWith("#")) {
                val content = raw.tint.substring(1)
                val value = content.toInt(16)
                builder = builder.tint(value)
            } else throw RuntimeException("Invalid tint")
        }
        if (raw.attributes != null) {
            for (attribute in raw.attributes) {
                builder = builder.with(parseChemicalAttribute(attribute))
            }
        }

        return ParsedChemicalConfig(raw.name, builder)
    }

    fun parseJson(content: String): List<ParsedChemicalConfig> {
        try {
            val parsed = Json.decodeFromString<List<ChemicalConfig>>(content)
            return parsed.map { parseChemical(it) }
        } catch (e: Throwable) {
            throw RuntimeException("Failed to parse json", e)
        }
    }
}
