package com.mrbysco.ghastcow.client.renderer;

import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.client.ClientClass;
import com.mrbysco.ghastcow.client.GhastCowRenderState;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GhastCowAuraLayer extends EnergySwirlLayer<GhastCowRenderState, GhastCowModel> {
	private static final ResourceLocation GHASTCOW_ARMOR = Constants.modLoc("textures/entity/ghastcow_armor.png");
	private final GhastCowModel model;

	public GhastCowAuraLayer(RenderLayerParent<GhastCowRenderState, GhastCowModel> parent, EntityModelSet modelSet) {
		super(parent);
		this.model = new GhastCowModel(modelSet.bakeLayer(ClientClass.GHAST_COW));
	}

	@Override
	protected boolean isPowered(GhastCowRenderState renderState) {
		return renderState.isPowered;
	}

	@Override
	protected float xOffset(float offset) {
		return Mth.cos(offset * 0.02F) * 3.0F;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return GHASTCOW_ARMOR;
	}

	@Override
	protected GhastCowModel model() {
		return this.model;
	}
}
