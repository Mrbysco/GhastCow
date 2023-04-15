package com.mrbysco.ghastcow;

import com.mrbysco.ghastcow.client.ClientClass;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.client.renderer.GhastCowRenderer;
import com.mrbysco.ghastcow.registration.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class GhastCowFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(ClientClass.GHAST_COW, GhastCowModel::createMesh);
		EntityRendererRegistry.register(ModEntities.GHAST_COW.get(), (ctx) -> new GhastCowRenderer(ctx));
	}
}
