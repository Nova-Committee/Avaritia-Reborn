package committee.nova.mods.avaritia.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.common.entity.BladeSlashEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.RenderStateShard.*;

/**
 * @Project: Avaritia
 * @Author: cnlimiter
 * @CreateTime: 2024/11/16 01:41
 * @Description:
 */
@OnlyIn(Dist.CLIENT)
public class BladeSlashRender extends EntityRenderer<BladeSlashEntity> {

    private static final ResourceLocation TEXTURE = Static.rl("textures/entity/blade_slash.png");
    private static final RenderType RENDER_TYPE = RenderType.create("blade_projectile",
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
            RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                    .setShaderState(RENDERTYPE_TEXT_SEE_THROUGH_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .createCompositeState(true));

    public BladeSlashRender(EntityRendererProvider.Context ctx) {

        super(ctx);
    }

    @Override
    public void render(BladeSlashEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource buffer, int packedLight) {

        matrixStackIn.pushPose();

        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 10));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(entity.zRot));
        matrixStackIn.scale(0.6F, 0.6F, 1.8F);
        PoseStack.Pose matrixStackEntry = matrixStackIn.last();
        Matrix4f pose = matrixStackEntry.pose();
        Matrix3f normal = matrixStackEntry.normal();
        VertexConsumer builder = buffer.getBuffer(RENDER_TYPE);

        packedLight = 0x00F000F0;
        this.vertex(pose, normal, builder, 1, 0, 1, 1, 0, 0, 1, 0, packedLight);
        this.vertex(pose, normal, builder, 1, 0, -1, 0, 0, 0, 1, 0, packedLight);
        this.vertex(pose, normal, builder, -1, 0, -1, 0, 1, 0, 1, 0, packedLight);
        this.vertex(pose, normal, builder, -1, 0, 1, 1, 1, 0, 1, 0, packedLight);

        matrixStackIn.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BladeSlashEntity entity) {
        return TEXTURE;
    }

    public void vertex(Matrix4f pose, Matrix3f normal, VertexConsumer builder, float x, float y, float z, float u, float v, int nx, int nz, int ny, int packedLight) {

        builder.vertex(pose, x, y, z).color(255, 255, 255, 200).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
    }

}
