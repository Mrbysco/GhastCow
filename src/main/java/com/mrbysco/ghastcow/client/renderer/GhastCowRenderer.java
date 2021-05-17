package com.mrbysco.ghastcow.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.ghastcow.GhastCowMod;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.entity.GhastCowEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GhastCowRenderer extends MobRenderer<GhastCowEntity, GhastCowModel<GhastCowEntity>> {
	private static final ResourceLocation INVULNERABLE_GHASTCOW_TEXTURES = new ResourceLocation(GhastCowMod.MOD_ID, "textures/entity/ghastcow_invulnerable.png");
	private static final ResourceLocation GHASTCOW_TEXTURES = new ResourceLocation(GhastCowMod.MOD_ID, "textures/entity/ghastcow.png");
	private static final ResourceLocation GHASTCOW_SHOOTING_TEXTURES = new ResourceLocation(GhastCowMod.MOD_ID, "textures/entity/ghastcow_shooting.png");

	public GhastCowRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GhastCowModel<>(), 1.5F);
		this.addLayer(new GhastCowAuraLayer(this));
	}

	protected int getBlockLightLevel(GhastCowEntity entityIn, BlockPos pos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(GhastCowEntity ghastCow) {
		int i = ghastCow.getInvulTime();
		return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_GHASTCOW_TEXTURES : ghastCow.isAttacking() ? GHASTCOW_SHOOTING_TEXTURES : GHASTCOW_TEXTURES;
	}

	@Override
	protected void scale(GhastCowEntity ghastCowEntity, MatrixStack matrixStackIn, float partialTickTime) {
		float f = 2.5F;
		int i = ghastCowEntity.getInvulTime();
		if (i > 0) {
			f -= ((float)i - partialTickTime) / 220.0F * 0.5F;
		}

		matrixStackIn.scale(f, f, f);
	}
}
