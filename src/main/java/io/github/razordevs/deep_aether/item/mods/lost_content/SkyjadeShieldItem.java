package io.github.razordevs.deep_aether.item.mods.lost_content;

import net.minecraft.world.item.ItemStack;
import io.github.razordevs.deep_aether.DeepAetherConfig;

public class SkyjadeShieldItem extends LCDAShieldItem {
    public SkyjadeShieldItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return DeepAetherConfig.SERVER.skyjade_enchant.get();
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return DeepAetherConfig.SERVER.skyjade_enchant.get();
    }
}
