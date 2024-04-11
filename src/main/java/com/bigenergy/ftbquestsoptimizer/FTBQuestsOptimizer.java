package com.bigenergy.ftbquestsoptimizer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FTBQuestsOptimizer.MODID)
public class FTBQuestsOptimizer {

    public static final String MODID = "ftbqoptimizer";
    public static final Logger LOGGER = LogManager.getLogger("FTB Quests Optimizer");

    public FTBQuestsOptimizer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Enabling FTB Quests Optimizer (legacy)");
    }

}
