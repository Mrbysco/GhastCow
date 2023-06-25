package com.mrbysco.ghastcow.client;

import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.client.renderer.GhastCowRenderer;
import com.mrbysco.ghastcow.registration.ModEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.GHAST_COW.get(), GhastCowRenderer::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ClientClass.GHAST_COW, () -> GhastCowModel.createMesh());
	}
}
