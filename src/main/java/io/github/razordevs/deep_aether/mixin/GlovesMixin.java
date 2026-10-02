package io.github.razordevs.deep_aether.mixin;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.item.accessories.gloves.GlovesItem;
import com.google.common.collect.Multimap;
import io.github.razordevs.deep_aether.DeepAether;
import io.github.razordevs.deep_aether.datagen.registry.DAEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

@Mixin(GlovesItem.class)
public abstract class GlovesMixin extends Item {

    @Unique
    private static final ResourceLocation EXTRA_BLOCK_REACH_ID = ResourceLocation.fromNamespaceAndPath(DeepAether.MODID, "extra_block_reach");


    public GlovesMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(at = @At(value = "TAIL"), method = "getAttributeModifiers(Ltop/theillusivec4/curios/api/SlotContext;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/ItemStack;)Lcom/google/common/collect/Multimap;", remap = false)
    private void deepAether$addExtraBlockReach(SlotContext slotContext, ResourceLocation id, ItemStack stack, CallbackInfoReturnable<Multimap<Holder<Attribute>, AttributeModifier>> cir) {
        if (slotContext.entity() == null) {
            return;
        }
        cir.getReturnValue().put(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(EXTRA_BLOCK_REACH_ID, stack.getEnchantmentLevel(slotContext.entity().level().holderOrThrow(DAEnchantments.GLOVES_REACH)), AttributeModifier.Operation.ADD_VALUE));
    }
}
