package com.mrbysco.ghastcow.client.renderer;

import com.mrbysco.ghastcow.GhastCowMod;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.entity.GhastCowEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GhastCowAuraLayer extends EnergyLayer<GhastCowEntity, GhastCowModel<GhastCowEntity>> {
	private static final ResourceLocation GHASTCOW_ARMOR = new ResourceLocation(GhastCowMod.MOD_ID, "textures/entity/ghastcow_armor.png");
	private final GhastCowModel<GhastCowEntity> witherModel = new GhastCowModel<>(0.5F);

	public GhastCowAuraLayer(IEntityRenderer<GhastCowEntity, GhastCowModel<GhastCowEntity>> p_i50915_1_) {
		super(p_i50915_1_);
	}

	protected float xOffset(float p_225634_1_) {
		return MathHelper.cos(p_225634_1_ * 0.02F) * 3.0F;
	}

	protected ResourceLocation getTextureLocation() {
		return GHASTCOW_ARMOR;
	}

	protected EntityModel<GhastCowEntity> model() {
		return this.witherModel;
	}
}
