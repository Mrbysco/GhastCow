package com.mrbysco.ghastcow;

import com.mojang.logging.LogUtils;
import com.mrbysco.ghastcow.client.ClientHandler;
import com.mrbysco.ghastcow.config.GhowConfig;
import com.mrbysco.ghastcow.handler.SpawnHandler;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(GhastCowMod.MOD_ID)
public class GhastCowMod {
	public static final String MOD_ID = "ghastcow";
	public static final Logger LOGGER = LogUtils.getLogger();

	public GhastCowMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GhowConfig.serverSpec);
		eventBus.register(GhowConfig.class);

		eventBus.addListener(this::setup);
		eventBus.addListener(ModEntities::registerEntityAttributes);

		MinecraftForge.EVENT_BUS.register(new SpawnHandler());

		ModEntities.ENTITY_TYPES.register(eventBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		});
	}

	private void setup(final FMLCommonSetupEvent event) {
		ModEntities.registerSpawnPlacement();
	}
}
