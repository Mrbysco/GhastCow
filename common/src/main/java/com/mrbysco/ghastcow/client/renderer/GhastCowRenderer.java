package com.mrbysco.ghastcow.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.ghastcow.Constants;
import com.mrbysco.ghastcow.client.ClientClass;
import com.mrbysco.ghastcow.client.GhastCowRenderState;
import com.mrbysco.ghastcow.client.model.GhastCowModel;
import com.mrbysco.ghastcow.entity.GhastCow;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class GhastCowRenderer extends MobRenderer<GhastCow, GhastCowRenderState, GhastCowModel> {
	private static final Identifier INVULNERABLE_GHASTCOW_TEXTURES = Constants.modLoc("textures/entity/ghastcow_invulnerable.png");
	private static final Identifier GHASTCOW_TEXTURES = Constants.modLoc("textures/entity/ghastcow.png");
	private static final Identifier GHASTCOW_SHOOTING_TEXTURES = Constants.modLoc("textures/entity/ghastcow_shooting.png");

	public GhastCowRenderer(Context context) {
		super(context, new GhastCowModel(context.bakeLayer(ClientClass.GHAST_COW)), 1.5F);
		this.addLayer(new GhastCowAuraLayer(this, context.getModelSet()));
	}

	protected int getBlockLightLevel(GhastCow entityIn, BlockPos pos) {
		return 15;
	}

	@Override
	public GhastCowRenderState createRenderState() {
		return new GhastCowRenderState();
	}

	@Override
	public void extractRenderState(GhastCow ghastCow, GhastCowRenderState renderState, float partialTicks) {
		super.extractRenderState(ghastCow, renderState, partialTicks);
		int i = ghastCow.getInvulnerableTicks();
		renderState.invulnerableTicks = i > 0 ? (float)i - partialTicks : 0.0F;
		renderState.isPowered = ghastCow.isPowered();
		renderState.isAttacking = ghastCow.isAttacking();
	}

	@Override
	public Identifier getTextureLocation(GhastCowRenderState renderState) {
		int i = Mth.floor(renderState.invulnerableTicks);
		return i > 0 && (i > 80 || i / 5 % 2 != 1) ?
				INVULNERABLE_GHASTCOW_TEXTURES :
				renderState.isAttacking ?
						GHASTCOW_SHOOTING_TEXTURES :
						GHASTCOW_TEXTURES;
	}

	@Override
	protected void scale(GhastCowRenderState renderState, PoseStack poseStack) {
		float f = 2.0F;
		if (renderState.invulnerableTicks > 0.0F) {
			f -= renderState.invulnerableTicks / 220.0F * 0.5F;
		}
		poseStack.scale(f, f, f);
	}
}
