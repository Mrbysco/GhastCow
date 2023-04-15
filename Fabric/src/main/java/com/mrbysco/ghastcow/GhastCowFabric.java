package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.callback.LivingDeathCallback;
import com.mrbysco.ghastcow.config.GhowConfigFabric;
import com.mrbysco.ghastcow.entity.GhastCow;
import com.mrbysco.ghastcow.registration.ModEntities;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.InteractionResult;

public class GhastCowFabric implements ModInitializer {
	public static ConfigHolder<GhowConfigFabric> config;

	@Override
	public void onInitialize() {
		config = AutoConfig.register(GhowConfigFabric.class, Toml4jConfigSerializer::new);

		CommonClass.init();

		FabricDefaultAttributeRegistry.register(ModEntities.GHAST_COW.get(), GhastCow.generateAttributes());

		LivingDeathCallback.EVENT.register((living, source) -> {
			CommonClass.onDeath(living, source);
			return InteractionResult.PASS;
		});
	}
}
