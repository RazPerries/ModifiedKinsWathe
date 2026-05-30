package org.BsXinQin.kinswathe.items;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerPoisonComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.BsXinQin.kinswathe.KinsWatheItems;
import org.BsXinQin.kinswathe.KinsWatheRoles;
import org.agmas.harpymodloader.component.WorldModifierComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MedicalKitItem extends Item {

    public MedicalKitItem(@NotNull Settings settings) {super(settings);}


    @Override
    public @NotNull TypedActionResult<@NotNull ItemStack> use(@NotNull World world, @NotNull PlayerEntity player, @NotNull Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!player.getWorld().isClient && player.isSneaking()) {
            PlayerPoisonComponent playerPoison = PlayerPoisonComponent.KEY.get(player);
            if (!player.isInCreativeMode()) {
                KinsWatheItems.setItemAfterUsing(player, this, null);
            }
            itemStack.decrementUnlessCreative(1, player);
            player.sendMessage(Text.literal("You used the Medkit on yourself.").withColor(Color.GREEN.getRGB()), true);
            playerPoison.reset();
            player.playSoundToPlayer(SoundEvents.ENTITY_HORSE_ARMOR, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return TypedActionResult.success(itemStack, player.getWorld().isClient);
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, @NotNull PlayerEntity player, @NotNull LivingEntity entity, Hand hand) {
        if (player.getItemCooldownManager().isCoolingDown(this)) return ActionResult.FAIL;
        if (!player.getWorld().isClient && entity instanceof @NotNull PlayerEntity targetPlayer) {
            PlayerPoisonComponent targetPoison = PlayerPoisonComponent.KEY.get(targetPlayer);
            if (!player.isInCreativeMode()) {
                KinsWatheItems.setItemAfterUsing(player, this, null);
            }
            player.getStackInHand(hand).decrementUnlessCreative(1, player);
            targetPoison.reset();
            player.playSoundToPlayer(SoundEvents.ENTITY_HORSE_ARMOR, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}