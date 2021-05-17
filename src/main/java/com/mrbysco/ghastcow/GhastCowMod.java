package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.client.ClientHandler;
import com.mrbysco.ghastcow.handler.SpawnHandler;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GhastCowMod.MOD_ID)
public class GhastCowMod {
    public static final String MOD_ID = "ghastcow";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public GhastCowMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);
        eventBus.addListener(ModEntities::registerEntityAttributes);

        MinecraftForge.EVENT_BUS.register(new SpawnHandler());

        ModEntities.ENTITIES.register(eventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModEntities.registerSpawnPlacement();
    }
}
