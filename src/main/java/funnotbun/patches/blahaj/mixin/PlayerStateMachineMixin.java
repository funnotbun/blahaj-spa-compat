package funnotbun.patches.blahaj.mixin;

import com.huaxiaosheng.smoothplayeranimations.state.ActionController;
import com.huaxiaosheng.smoothplayeranimations.state.PlayerStateMachine;
import com.huaxiaosheng.smoothplayeranimations.state.UpperBodyMode;
import com.huaxiaosheng.smoothplayeranimations.state.rules.Environment;
import hibi.blahaj.block.CuddlyItem;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Lets Red's CROSSBOW_HOLD arm pose survive SPA's upper and base animation layers. */
@Mixin(value = PlayerStateMachine.class, remap = false)
public abstract class PlayerStateMachineMixin {
    @Shadow @Final private ActionController action;

    @Unique private boolean blahajSpaCompat$wasCuddling;

    @Inject(method = "tickUpperBody", at = @At("HEAD"), cancellable = true, remap = false)
    private void blahajSpaCompat$yieldArms(AbstractClientPlayer player, Environment environment,
                                             CallbackInfoReturnable<UpperBodyMode> cir) {
        boolean cuddling = player.getMainHandItem().getItem() instanceof CuddlyItem
                || player.getOffhandItem().getItem() instanceof CuddlyItem;

        if (cuddling) {
            if (!blahajSpaCompat$wasCuddling) {
                action.forceRelease(player, 0);
            }
            blahajSpaCompat$wasCuddling = true;
            cir.setReturnValue(UpperBodyMode.YIELD);
        } else {
            blahajSpaCompat$wasCuddling = false;
        }
    }

    // SPA resets YIELD during its sprint-stop state; keep the arm mask for that tick too.
    @ModifyArg(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lcom/huaxiaosheng/smoothplayeranimations/state/PlayerStateMachine;replayIfChanged(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/huaxiaosheng/smoothplayeranimations/state/rules/Environment;Lcom/huaxiaosheng/smoothplayeranimations/state/rules/LocomotionState;ZZZI)V"),
            index = 5,
            remap = false
    )
    private boolean blahajSpaCompat$keepArmsYielded(boolean disableArms) {
        return disableArms || blahajSpaCompat$wasCuddling;
    }
}
