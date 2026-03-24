package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.callback.LivingDeathCallback;
import com.mrbysco.ghastcow.config.GhowConfig;
import com.mrbysco.ghastcow.entity.GhastCow;
import com.mrbysco.ghastcow.registration.ModEntities;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.InteractionResult;
import net.neoforged.fml.config.ModConfig;

public class GhastCowFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, GhowConfig.commonSpec);

		CommonClass.init();

		FabricDefaultAttributeRegistry.register(ModEntities.GHAST_COW.get(), GhastCow.generateAttributes());

		LivingDeathCallback.EVENT.register((living, source) -> {
			CommonClass.onDeath(living, source);
			return InteractionResult.PASS;
		});
	}
}
