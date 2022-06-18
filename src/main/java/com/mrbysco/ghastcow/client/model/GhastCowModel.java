package com.mrbysco.ghastcow.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class GhastCowModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart[] tentacles = new ModelPart[9];

	public GhastCowModel(ModelPart part) {
		this.root = part;
		this.head = part.getChild("head");
		this.body = part.getChild("body");

		for (int i = 0; i < this.tentacles.length; ++i) {
			this.tentacles[i] = part.getChild(createTentacleName(i));
		}
	}

	public static LayerDefinition createMesh() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(18, 4).addBox(-6.0F, -10.0F, -20.0F, 12.0F, 18.0F, 10.0F)
						.texOffs(52, 0).addBox(-2.0F, 2.0F, -21.0F, 4.0F, 6.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F));


		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 9.0F, -6.0F, 8.0F, 8.0F, 6.0F)
						.texOffs(22, 0).addBox(4.0F, 8.0F, -4.0F, 1.0F, 3.0F, 1.0F)
						.texOffs(22, 0).addBox(-5.0F, 8.0F, -4.0F, 1.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 4.0F, -8.0F));


		Random random = new Random(1660L);

		for (int i = 0; i < 9; ++i) {
			float f = (((float) (i % 3) - (float) (i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
			float f1 = ((float) (i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
			int j = random.nextInt(7) + 8;
			partdefinition.addOrReplaceChild(createTentacleName(i), CubeListBuilder.create()
							.texOffs(2, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, (float) j, 2.0F),
					PartPose.offset(f, 24.6F, f1));
		}

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	private static String createTentacleName(int i) {
		return "tentacle" + i;
	}

	private static void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		for (int i = 0; i < this.tentacles.length; ++i) {
			this.tentacles[i].xRot = 0.2F * Mth.sin(ageInTicks * 0.3F + (float) i) + 0.4F;
		}
	}

	public ModelPart root() {
		return this.root;
	}
}