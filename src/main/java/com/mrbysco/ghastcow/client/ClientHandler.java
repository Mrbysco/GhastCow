package com.mrbysco.ghastcow.client;

import com.mrbysco.ghastcow.client.renderer.GhastCowRenderer;
import com.mrbysco.ghastcow.registry.ModEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.GHAST_COW.get(), GhastCowRenderer::new);
	}
}
