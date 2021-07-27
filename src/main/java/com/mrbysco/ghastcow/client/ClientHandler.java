package com.mrbysco.ghastcow.client;

import com.mrbysco.ghastcow.GhastCowMod;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.client.renderer.GhastCowRenderer;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	public static final ModelLayerLocation GHAST_COW = new ModelLayerLocation(new ResourceLocation(GhastCowMod.MOD_ID, "ghast_cow"), "ghast_cow");

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.GHAST_COW.get(), GhastCowRenderer::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GHAST_COW, () -> GhastCowModel.createMesh());
	}
}
