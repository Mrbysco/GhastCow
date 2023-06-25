package com.mrbysco.ghastcow.client.renderer;

import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.client.ClientClass;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GhastCowAuraLayer extends EnergySwirlLayer<GhastCow, GhastCowModel<GhastCow>> {
	private static final ResourceLocation GHASTCOW_ARMOR = new ResourceLocation(Constants.MOD_ID, "textures/entity/ghastcow_armor.png");
	private final GhastCowModel<GhastCow> model;

	public GhastCowAuraLayer(RenderLayerParent<GhastCow, GhastCowModel<GhastCow>> parent, EntityModelSet modelSet) {
		super(parent);
		this.model = new GhastCowModel<>(modelSet.bakeLayer(ClientClass.GHAST_COW));
	}

	protected float xOffset(float offset) {
		return Mth.cos(offset * 0.02F) * 3.0F;
	}

	protected ResourceLocation getTextureLocation() {
		return GHASTCOW_ARMOR;
	}

	protected EntityModel<GhastCow> model() {
		return this.model;
	}
}
