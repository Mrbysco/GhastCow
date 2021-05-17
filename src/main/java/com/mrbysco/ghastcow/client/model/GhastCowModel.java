package com.mrbysco.ghastcow.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class GhastCowModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer headModel;
	private final ModelRenderer[] tentacles = new ModelRenderer[9];
	private final ImmutableList<ModelRenderer> partList;

	public GhastCowModel() {
		this(0.0F);
	}

	public GhastCowModel(float scale) {
		texWidth = 64;
		texHeight = 32;

		Builder<ModelRenderer> builder = ImmutableList.builder();

		body = new ModelRenderer(this);
		body.setPos(0.0F, 5.0F, 2.0F);
		setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
		body.texOffs(18, 4).addBox(-6.0F, -10.0F, -20.0F, 12.0F, 18.0F, 10.0F, scale, false);
		body.texOffs(52, 0).addBox(-2.0F, 2.0F, -21.0F, 4.0F, 6.0F, 1.0F, scale, false);
		builder.add(body);

		headModel = new ModelRenderer(this);
		headModel.setPos(0.0F, 4.0F, -8.0F);
		headModel.texOffs(0, 0).addBox(-4.0F, 9.0F, -6.0F, 8.0F, 8.0F, 6.0F, scale, false);
		headModel.texOffs(22, 0).addBox(4.0F, 8.0F, -4.0F, 1.0F, 3.0F, 1.0F, scale, false);
		headModel.texOffs(22, 0).addBox(-5.0F, 8.0F, -4.0F, 1.0F, 3.0F, 1.0F, scale, false);
		builder.add(headModel);
		Random random = new Random(1660L);

		for(int i = 0; i < this.tentacles.length; ++i) {
			this.tentacles[i] = new ModelRenderer(this, 2, 18);
			float f = (((float)(i % 3) - (float)(i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
			float f1 = ((float)(i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
			int j = random.nextInt(7) + 8;
			this.tentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2.0F, (float)j, 2.0F, scale);
			this.tentacles[i].x = f;
			this.tentacles[i].z = f1;
			this.tentacles[i].y = 24.6F;
			builder.add(this.tentacles[i]);
		}

		partList = builder.build();
	}

	public Iterable<ModelRenderer> parts() {
		return this.partList;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		for(int i = 0; i < this.tentacles.length; ++i) {
			this.tentacles[i].xRot = 0.2F * MathHelper.sin(ageInTicks * 0.3F + (float)i) + 0.4F;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}