package com.mrbysco.ghastcow.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.client.ClientClass;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class GhastCowRenderer extends MobRenderer<GhastCow, GhastCowModel<GhastCow>> {
	private static final ResourceLocation INVULNERABLE_GHASTCOW_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/ghastcow_invulnerable.png");
	private static final ResourceLocation GHASTCOW_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/ghastcow.png");
	private static final ResourceLocation GHASTCOW_SHOOTING_TEXTURES = new ResourceLocation(Constants.MOD_ID, "textures/entity/ghastcow_shooting.png");

	public GhastCowRenderer(Context context) {
		super(context, new GhastCowModel(context.bakeLayer(ClientClass.GHAST_COW)), 1.5F);
		this.addLayer(new GhastCowAuraLayer(this, context.getModelSet()));
	}

	protected int getBlockLightLevel(GhastCow entityIn, BlockPos pos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(GhastCow ghastCow) {
		int i = ghastCow.getInvulnerableTicks();
		return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_GHASTCOW_TEXTURES : ghastCow.isAttacking() ? GHASTCOW_SHOOTING_TEXTURES : GHASTCOW_TEXTURES;
	}

	@Override
	protected void scale(GhastCow ghastCowEntity, PoseStack matrixStackIn, float partialTickTime) {
		float f = 2.5F;
		int i = ghastCowEntity.getInvulnerableTicks();
		if (i > 0) {
			f -= ((float) i - partialTickTime) / 220.0F * 0.5F;
		}

		matrixStackIn.scale(f, f, f);
	}
}
