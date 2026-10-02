package io.github.razordevs.deep_aether.client.renderer.accessory;

import com.aetherteam.aether.client.renderer.AetherModelLayers;
import com.aetherteam.aether.client.renderer.accessory.GlovesRenderer;
import com.aetherteam.aether.client.renderer.accessory.model.GlovesModel;
import com.aetherteam.aether.item.accessories.gloves.GlovesItem;
import com.aetherteam.aether.mixin.mixins.client.accessor.PlayerModelAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.razordevs.deep_aether.networking.attachment.DAAttachments;
import io.github.razordevs.deep_aether.networking.attachment.DAPlayerAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SkyjadeGlovesRenderer extends GlovesRenderer {
    private final GlovesModel glovesModel;
    private final GlovesModel glovesTrimModel;
    private final GlovesModel glovesModelSlim;
    private final GlovesModel glovesTrimModelSlim;
    private final GlovesModel glovesFirstPerson;
    private final GlovesModel glovesTrimFirstPerson;
    private final TextureAtlas armorTrimAtlas;

    public SkyjadeGlovesRenderer() {
        this.glovesModel = new GlovesModel(Minecraft.getInstance().getEntityModels().bakeLayer(AetherModelLayers.GLOVES));
        this.glovesTrimModel = new GlovesModel(Minecraft.getInstance().getEntityModels().bakeLayer(AetherModelLayers.GLOVES_TRIM));
        this.glovesModelSlim = new GlovesModel(Minecraft.getInstance().getEntityModels().bakeLayer(AetherModelLayers.GLOVES_SLIM));
        this.glovesTrimModelSlim = new GlovesModel(Minecraft.getInstance().getEntityModels().bakeLayer(AetherModelLayers.GLOVES_TRIM_SLIM));
        this.glovesFirstPerson = new GlovesModel(Minecraft.getInstance().getEntityModels().bakeLayer(AetherModelLayers.GLOVES_FIRST_PERSON));
        this.glovesTrimFirstPerson = new GlovesModel(Minecraft.getInstance().getEntityModels().bakeLayer(AetherModelLayers.GLOVES_TRIM_FIRST_PERSON));
        this.armorTrimAtlas = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }


    /**
     * Modified version of {@link GlovesRenderer#render(ItemStack, SlotContext, PoseStack, RenderLayerParent, MultiBufferSource, int, float, float, float, float, float, float)}
     */
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity livingEntity = slotContext.entity();
        GlovesItem glovesItem = (GlovesItem) stack.getItem();
        GlovesModel model = this.glovesModel;
        GlovesModel trimModel = this.glovesTrimModel;
        ResourceLocation texture = glovesItem.getGlovesTexture();

        M entityModel = renderLayerParent.getModel();
        if (entityModel instanceof PlayerModel<?> playerModel) {
            PlayerModelAccessor playerModelAccessor = (PlayerModelAccessor) playerModel;
            model = playerModelAccessor.aether$getSlim() ? this.glovesModelSlim : this.glovesModel;
            trimModel = playerModelAccessor.aether$getSlim() ? this.glovesTrimModelSlim : this.glovesTrimModel;
        }

        ICurioRenderer.followBodyRotations(slotContext.entity(), model);
        ICurioRenderer.followBodyRotations(slotContext.entity(), trimModel);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
        int color = -1;

        if(livingEntity.hasData(DAAttachments.PLAYER)) {
            DAPlayerAttachment attachment = livingEntity.getData(DAAttachments.PLAYER);
            if(attachment.isSkyjadeAbilityActivated() && attachment.hasSkyjadeSet()) {
                vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));
                color = FastColor.ARGB32.color(60, color);
            }
        }

        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);

        GlovesModel finalTrimModel = trimModel;

        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim != null) {
            TextureAtlasSprite textureAtlasSprite = this.armorTrimAtlas.getSprite(trim.outerTexture(glovesItem.getMaterial()));
            VertexConsumer trimConsumer = textureAtlasSprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            finalTrimModel.renderToBuffer(poseStack, trimConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        }

        if (stack.hasFoil()) {
            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.armorEntityGlint()), packedLight, OverlayTexture.NO_OVERLAY, color);
        }
    }

    /**
     * Modified version of {@link GlovesRenderer#renderFirstPerson(ItemStack, PoseStack, MultiBufferSource, int, AbstractClientPlayer, HumanoidArm)}
     */
    @Override
    public void renderFirstPerson(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, HumanoidModel<?> humanoidModel, HumanoidArm arm) {
        GlovesModel model = this.glovesFirstPerson;
        GlovesModel trimModel = this.glovesTrimFirstPerson;

        GlovesItem glovesItem = (GlovesItem) stack.getItem();

        int color = -1;

        model.setAllVisible(false);
        model.attackTime = 0.0F;
        model.crouching = false;
        model.swimAmount = 0.0F;
        model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        ModelPart gloveArm = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        gloveArm.visible = true;
        gloveArm.xRot = 0.0F;

        VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(glovesItem.getGlovesTexture()));


        //Added code
        if(player.hasData(DAAttachments.PLAYER)) {
            DAPlayerAttachment attachment = player.getData(DAAttachments.PLAYER);
            if(attachment.isSkyjadeAbilityActivated() && attachment.hasSkyjadeSet()) {
                consumer = buffer.getBuffer(RenderType.entityTranslucent(glovesItem.getGlovesTexture()));
                color = FastColor.ARGB32.color(60, color);
            }
        }

        gloveArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);

        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim != null) {
            trimModel.setAllVisible(false);
            trimModel.attackTime = 0.0F;
            trimModel.crouching = false;
            trimModel.swimAmount = 0.0F;
            trimModel.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

            ModelPart gloveTrimArm = arm == HumanoidArm.RIGHT ? trimModel.rightArm : trimModel.leftArm;
            gloveTrimArm.visible = true;
            gloveTrimArm.xRot = 0.0F;

            TextureAtlasSprite textureAtlasSprite = this.armorTrimAtlas.getSprite(trim.outerTexture(glovesItem.getMaterial()));
            VertexConsumer trimConsumer = textureAtlasSprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            gloveTrimArm.render(poseStack, trimConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
        if (stack.hasFoil()) {
            gloveArm.render(poseStack, buffer.getBuffer(RenderType.armorEntityGlint()), packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
