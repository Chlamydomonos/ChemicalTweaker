package xyz.chlamydomonos.chemicaltweaker

import mekanism.api.chemical.gas.GasBuilder
import mekanism.api.chemical.infuse.InfuseTypeBuilder
import mekanism.api.chemical.pigment.PigmentBuilder
import mekanism.api.chemical.slurry.SlurryBuilder
import net.minecraftforge.fml.loading.FMLPaths
import xyz.chlamydomonos.chemicaltweaker.config.ParsedChemicalConfig
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

object ConfigLoader {
    data class Config(
        val gases: ArrayList<ParsedChemicalConfig>,
        val slurries: ArrayList<ParsedChemicalConfig>,
        val pigments: ArrayList<ParsedChemicalConfig>,
        val infuseTypes: ArrayList<ParsedChemicalConfig>
    )

    private fun loadConfig(): Config {
        val gases = arrayListOf<ParsedChemicalConfig>()
        val slurries = arrayListOf<ParsedChemicalConfig>()
        val pigments = arrayListOf<ParsedChemicalConfig>()
        val infuseTypes = arrayListOf<ParsedChemicalConfig>()
        val configPath: Path
        try {
            val path = FMLPaths.CONFIGDIR.get().resolve(ChemicalTweaker.ID)
            if (!path.exists()) {
                path.createDirectories()
            } else if (!path.isDirectory()) {
                path.deleteExisting()
                path.createDirectories()
            }
            configPath = path
        } catch (e: Throwable) {
            ChemicalTweaker.LOGGER.error("Error loading config directory", e)
            return Config(gases, slurries, pigments, infuseTypes)
        }
        val count = Files.walk(configPath).use { stream ->
            stream
                .filter(Files::isRegularFile)
                .filter { it.fileName.toString().endsWith(".json") }
                .map {
                    val content = Files.readString(it)
                    try {
                        val parsed = ConfigParser.parseJson(content)
                        for (config in parsed) {
                            when (config.builder) {
                                is GasBuilder -> gases.add(config)
                                is SlurryBuilder -> slurries.add(config)
                                is PigmentBuilder -> pigments.add(config)
                                is InfuseTypeBuilder -> infuseTypes.add(config)
                            }
                        }
                    } catch (e: Throwable) {
                        ChemicalTweaker.LOGGER.error("Error loading ${it.fileName}", e)
                    }
                    true
                }.count()
        }
        ChemicalTweaker.LOGGER.info("Found $count json files")
        return Config(gases, slurries, pigments, infuseTypes)
    }

    private val config by lazy { loadConfig() }

    val gases get() = config.gases
    val slurries get() = config.slurries
    val pigments get() = config.pigments
    val infuseTypes get() = config.infuseTypes
}