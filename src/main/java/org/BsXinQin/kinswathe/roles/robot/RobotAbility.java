package org.BsXinQin.kinswathe.roles.robot;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.BsXinQin.kinswathe.KinsWatheConfig;
import org.BsXinQin.kinswathe.KinsWatheRoles;
import org.BsXinQin.kinswathe.component.AbilityPlayerComponent;
import org.jetbrains.annotations.NotNull;

public class RobotAbility {

    public static void register(@NotNull PlayerEntity player) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(player.getWorld());
        AbilityPlayerComponent ability = AbilityPlayerComponent.KEY.get(player);
        if (gameWorld.isRole(player, KinsWatheRoles.ROBOT) && GameFunctions.isPlayerAliveAndSurvival(player) && ability.cooldown <= 0) {
            PlayerShopComponent playerShopComponent = PlayerShopComponent.KEY.get(player);
            RobotComponent robotComponent = RobotComponent.KEY.get(player);
            PlayerMoodComponent playerMoodComponent = PlayerMoodComponent.KEY.get(player);
            if (playerShopComponent.balance > robotComponent.calibrationCost) {
                playerMoodComponent.setMood(1f);
                robotComponent.setPoisonImmunity(true);
                robotComponent.setCalibrationTicks(GameConstants.getInTicks(0, 10));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, KinsWatheConfig.HANDLER.instance().RobotAbilityDuration * 20, 0, false, false, true));
                player.playSoundToPlayer(SoundEvents.ENTITY_IRON_GOLEM_HURT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                playerShopComponent.setBalance(playerShopComponent.balance - robotComponent.calibrationCost);
                ability.setAbilityCooldown(GameConstants.getInTicks(1,30)/20);
            } else {
                player.sendMessage(Text.translatable("tip.kinswathe.ability.not_enough_money", robotComponent.calibrationCost).formatted(Formatting.DARK_RED), true);

            }
        }
    }
}