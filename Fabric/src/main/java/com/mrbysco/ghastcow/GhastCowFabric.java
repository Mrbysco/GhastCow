package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.callback.LivingDeathCallback;
import com.mrbysco.ghastcow.config.GhowConfigFabric;
import com.mrbysco.ghastcow.entity.GhastCow;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class GhastCowFabric implements ModInitializer {
	public static ConfigHolder<GhowConfigFabric> config;

	public static final EntityType<GhastCow> GHAST_COW = EntityType.Builder.<GhastCow>of(GhastCow::new, MobCategory.MONSTER)
			.sized(2.5F, 1.5F).clientTrackingRange(10).build("ghast_cow");

	@Override
	public void onInitialize() {
		config = AutoConfig.register(GhowConfigFabric.class, Toml4jConfigSerializer::new);

		Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, "ghast_cow"), GHAST_COW);
		FabricDefaultAttributeRegistry.register(GHAST_COW, GhastCow.generateAttributes());

		LivingDeathCallback.EVENT.register((living, source) -> {
			CommonClass.onDeath(living, source);
			return InteractionResult.PASS;
		});
	}
}
