package net.creativityshark.losers_bracket.entity.glare;

import com.google.common.collect.ImmutableList;
import com.ibm.icu.text.Normalizer2;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import org.w3c.dom.Text;

public class GlareEntityModel extends EntityModel<GlareEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart cover;
    private final ModelPart sleep;
    private final ModelPart tail0;
    private final ModelPart tail1;
    private final ModelPart tail2;

    public GlareEntityModel(ModelPart modelPart) {
        this.root = modelPart;
        this.head = modelPart.getChild("head");
        this.cover = head.getChild("cover");
        this.sleep = head.getChild("sleep");
        this.tail0 = modelPart.getChild("tail0");
        this.tail1 = modelPart.getChild("tail1");
        this.tail2 = modelPart.getChild("tail2");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-5f, -5f, -5f, 10f, 10f, 10f, false), ModelTransform.pivot(0,2,0));
        head.addChild("cover", ModelPartBuilder.create().uv(0,20).cuboid(-6,6,-6,12,12,12,false), ModelTransform.pivot(0,-12,0));
        head.addChild("sleep", ModelPartBuilder.create().uv(0, 59).cuboid(-5,8,-4.005f,10,5,0,false), ModelTransform.pivot(0,-9,0));
        root.addChild("tail0", ModelPartBuilder.create().uv(0,44).cuboid(-4,3,-4,8,3,8,false), ModelTransform.pivot(0,5,0));
        root.addChild("tail1", ModelPartBuilder.create().uv(41,52).cuboid(-2,0,-2,4,6,4,false), ModelTransform.pivot(0,8,0));
        root.addChild("tail2", ModelPartBuilder.create().uv(37,21).cuboid(-1,4,0,2,10,0,false), ModelTransform.pivot(0,4,0));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(GlareEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.setAngles(0, 0, 0);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.translate(0, 0, 0);
        ImmutableList.of(this.root).forEach((modelRenderer) -> {
            modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }
}

