package io.github.razordevs.deep_aether.item.gear.other;

import com.aetherteam.aether.item.accessories.pendant.PendantItem;
import io.github.razordevs.deep_aether.DeepAether;
import io.github.razordevs.deep_aether.init.DAMobEffects;
import io.github.razordevs.deep_aether.init.DASounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class MedalOfHonor extends PendantItem {
    public MedalOfHonor(Properties properties) {
        super(ResourceLocation.fromNamespaceAndPath(DeepAether.MODID, "medal_of_honor"), DASounds.ITEM_ACCESSORY_EQUIP_MEDAL_OF_HONOR, properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        slotContext.entity().addEffect(new MobEffectInstance(DAMobEffects.VALKYRIE_VALOR, 5, 0, true, false, true));
    }
}
