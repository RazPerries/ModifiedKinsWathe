package org.BsXinQin.kinswathe.mixin.roles.robot;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerPoisonComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.BsXinQin.kinswathe.KinsWatheRoles;
import org.BsXinQin.kinswathe.roles.robot.RobotComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerPoisonComponent.class)
public class RobotNoPoisonMixin {

    @Shadow @Final @NotNull private PlayerEntity player;

    @Inject(method = "setPoisonTicks", at = @At("HEAD"), cancellable = true)
    private void noRobotPoison(int ticks, UUID poisoner, @NotNull CallbackInfo ci) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(this.player.getWorld());
        if (gameWorld.isRole(this.player, KinsWatheRoles.ROBOT)) {
            RobotComponent robotComponent = RobotComponent.KEY.get(player);
            if (robotComponent.poisonImmune) {
                robotComponent.setPoisonImmunity(false);
                player.sendMessage(Text.literal("A harmful substance was detected in my systems. I should recalibrate in case it happens again.").withColor(KinsWatheRoles.ROBOT.color()), true);
                ci.cancel();
            }
            else {
                player.sendMessage(Text.literal("Error! Harmful substance detected.").withColor(KinsWatheRoles.ROBOT.color()), true);
            }
        }
    }
}