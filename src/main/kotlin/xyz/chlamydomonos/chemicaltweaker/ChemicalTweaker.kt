package xyz.chlamydomonos.chemicaltweaker

import com.mojang.logging.LogUtils
import mekanism.api.MekanismAPI
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasBuilder
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.infuse.InfuseTypeBuilder
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.pigment.PigmentBuilder
import mekanism.api.chemical.slurry.Slurry
import mekanism.api.chemical.slurry.SlurryBuilder
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.RegisterEvent

@Mod(ChemicalTweaker.ID)
@EventBusSubscriber(modid = ChemicalTweaker.ID, bus = EventBusSubscriber.Bus.MOD)
object ChemicalTweaker {
    const val ID = "chemicaltweaker"
    val LOGGER = LogUtils.getLogger()

    @SubscribeEvent
    fun onRegistryEvent(event: RegisterEvent) {
//        for (gas in ConfigLoader.gases) {
//            event.register(MekanismAPI.GAS_REGISTRY_NAME) {
//                it.register(gas.name, Gas(gas.builder as GasBuilder))
//            }
//        }
//        for (slurry in ConfigLoader.slurries) {
//            event.register(MekanismAPI.SLURRY_REGISTRY_NAME) {
//                it.register(slurry.name, Slurry(slurry.builder as SlurryBuilder))
//            }
//        }
//        for (pigment in ConfigLoader.pigments) {
//            event.register(MekanismAPI.PIGMENT_REGISTRY_NAME) {
//                it.register(pigment.name, Pigment(pigment.builder as PigmentBuilder))
//            }
//        }
//        for (infuseType in ConfigLoader.infuseTypes) {
//            event.register(MekanismAPI.INFUSE_TYPE_REGISTRY_NAME) {
//                it.register(infuseType.name, InfuseType(infuseType.builder as InfuseTypeBuilder))
//            }
//        }
        when (event.registryKey) {
            MekanismAPI.GAS_REGISTRY_NAME -> {
                LOGGER.info("registering gases")
                for (gas in ConfigLoader.gases) {
                    event.register(MekanismAPI.GAS_REGISTRY_NAME) {
                        it.register(gas.name, Gas(gas.builder as GasBuilder))
                    }
                }
            }
            MekanismAPI.SLURRY_REGISTRY_NAME -> {
                LOGGER.info("registering slurries")
                for (slurry in ConfigLoader.slurries) {
                    event.register(MekanismAPI.SLURRY_REGISTRY_NAME) {
                        it.register(slurry.name, Slurry(slurry.builder as SlurryBuilder))
                    }
                }
            }
            MekanismAPI.PIGMENT_REGISTRY_NAME -> {
                LOGGER.info("registering pigments")
                for (pigment in ConfigLoader.pigments) {
                    event.register(MekanismAPI.PIGMENT_REGISTRY_NAME) {
                        it.register(pigment.name, Pigment(pigment.builder as PigmentBuilder))
                    }
                }
            }
            MekanismAPI.INFUSE_TYPE_REGISTRY_NAME -> {
                LOGGER.info("registering infuse types")
                for (infuseType in ConfigLoader.infuseTypes) {
                    event.register(MekanismAPI.INFUSE_TYPE_REGISTRY_NAME) {
                        it.register(infuseType.name, InfuseType(infuseType.builder as InfuseTypeBuilder))
                    }
                }
            }
        }
    }
}