package net.fareskingtube.client.block.entity.renderer;

import net.fareskingtube.block.entity.custom.RevivalAltarBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class RevivalAltarBlockEntityRenderer implements BlockEntityRenderer<RevivalAltarBlockEntity> {
    public RevivalAltarBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(RevivalAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack stack = entity.getStack(0);
        if (stack.isEmpty()) return;

        matrices.push();
        matrices.translate(0.5, 1.25, 0.5);
        matrices.scale(0.6f, 0.6f, 0.6f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getRenderingRotation(1.5f)));

        itemRenderer.renderItem(
                stack,
                ModelTransformationMode.GUI,
                getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
