package org.BsXinQin.kinswathe.mixin.roles.robot;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerStaminaComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.BsXinQin.kinswathe.KinsWatheRoles;
import org.BsXinQin.kinswathe.roles.robot.RobotComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerStaminaComponent.class)
public class RobotStaminaRecoveryMixin {

    @Shadow @Final @NotNull private PlayerEntity player;

    @ModifyReturnValue(method = "getStaminaRegeneration", at = @At("RETURN"))
    private float robotStaminaRecovery(float original) {
        GameWorldComponent gameWorld = GameWorldComponent.KEY.get(this.player.getWorld());
        if (gameWorld.isRole(this.player, KinsWatheRoles.ROBOT)) {
            RobotComponent robotComponent = RobotComponent.KEY.get(this.player);
            if (robotComponent.calibrationTicks > 0) {
                return 1f;
            }
        }
        return original;
    }
}