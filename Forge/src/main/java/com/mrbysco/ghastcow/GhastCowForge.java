package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.client.ClientHandler;
import com.mrbysco.ghastcow.config.GhowConfig;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class GhastCowForge {

	public GhastCowForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GhowConfig.commonSpec);
		eventBus.register(GhowConfig.class);

		eventBus.addListener(ModEntities::registerSpawnPlacements);
		eventBus.addListener(ModEntities::registerEntityAttributes);

		MinecraftForge.EVENT_BUS.addListener(this::onDeath);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		});
	}

	private void onDeath(LivingDeathEvent event) {
		CommonClass.onDeath(event.getEntity(), event.getSource());
	}
}