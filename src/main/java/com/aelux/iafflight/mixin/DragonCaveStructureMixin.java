package com.aelux.iafflight.mixin;

import com.aelux.iafflight.dragon.DragonFlightConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "com.iafenvoy.iceandfire.world.structure.DragonCaveStructure")
public abstract class DragonCaveStructureMixin {
    @ModifyVariable(method = "addPieces", at = @At("STORE"), ordinal = 0)
    private int iafflight$applyCaveYOffset(int y) {
        return y + DragonFlightConfig.CAVE_Y_OFFSET;
    }
}
