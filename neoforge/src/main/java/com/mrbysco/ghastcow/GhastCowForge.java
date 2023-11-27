package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.client.ClientHandler;
import com.mrbysco.ghastcow.config.GhowConfig;
import com.mrbysco.ghastcow.registry.ModSetup;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@Mod(Constants.MOD_ID)
public class GhastCowForge {

	public GhastCowForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GhowConfig.commonSpec);
		eventBus.register(GhowConfig.class);

		CommonClass.init();

		eventBus.addListener(ModSetup::registerSpawnPlacements);
		eventBus.addListener(ModSetup::registerEntityAttributes);

		NeoForge.EVENT_BUS.addListener(this::onDeath);

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		}
	}

	private void onDeath(LivingDeathEvent event) {
		CommonClass.onDeath(event.getEntity(), event.getSource());
	}
}