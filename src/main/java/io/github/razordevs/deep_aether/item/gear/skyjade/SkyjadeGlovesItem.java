package io.github.razordevs.deep_aether.item.gear.skyjade;

import com.aetherteam.aether.inventory.AetherAccessorySlots;
import com.aetherteam.aether.item.accessories.gloves.GlovesItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.github.razordevs.deep_aether.DeepAether;
import io.github.razordevs.deep_aether.DeepAetherConfig;
import io.github.razordevs.deep_aether.init.DASounds;
import io.github.razordevs.deep_aether.item.gear.DAArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SkyjadeGlovesItem extends GlovesItem {
    public SkyjadeGlovesItem(double punchDamage, Item.Properties properties) {
        super(DAArmorMaterials.SKYJADE, punchDamage, ResourceLocation.fromNamespaceAndPath(DeepAether.MODID,"skyjade_gloves"), DASounds.ITEM_ARMOR_EQUIP_SKYJADE, properties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = LinkedHashMultimap.create();
        if (!DeepAetherConfig.SERVER.enable_skyjade_rework.get() && slotContext.identifier().equals(AetherAccessorySlots.GLOVES_SLOT)) {
            modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(DeepAether.MODID, "gloves_damage_bonus"), calculateIncrease(stack), AttributeModifier.Operation.ADD_VALUE));
        }
        return modifiers;
    }

    private float calculateIncrease(ItemStack stack) {
        int maxDurability = stack.getMaxDamage();
        int currentDurability = maxDurability - stack.getDamageValue();
        if (currentDurability >= maxDurability - (int) (maxDurability / 4.0)) {
            return 1F;

        }
        else if (currentDurability >= maxDurability - (int) (maxDurability / 3.0)) {
            return 0.75F;
        }

        else if (currentDurability >= maxDurability - (int) (maxDurability / 1.5)) {
            return 0.5F;
        }
        else {
            return 0.25F;
        }
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return DeepAetherConfig.SERVER.skyjade_enchant.get() && !DeepAetherConfig.SERVER.enable_skyjade_rework.get();
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return DeepAetherConfig.SERVER.skyjade_enchant.get() && !DeepAetherConfig.SERVER.enable_skyjade_rework.get();
    }
}
