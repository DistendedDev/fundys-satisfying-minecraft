package diztend.fundycopy.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public class TntEntityMixin {

    private Pair<Double, Double> polarToCartesian(double angle, double distance) {
        return new Pair<>(distance * Math.sin(angle), distance * Math.cos(angle));
    }

    private void spawnTnt(World world, double x, double y, double z, double force, int count) {
        for (int i = 0; i < count; i++) {
            double angle = ((double) i * 360) / count;
            Pair<Double, Double> velocityXZ = polarToCartesian(Math.toRadians(angle), force);
            System.out.println(i);
            System.out.println(angle);
            System.out.println(velocityXZ);
            TntEntity tnt = new TntEntity(world, x, y, z, null);
            world.spawnEntity(tnt);
            tnt.addVelocity(velocityXZ.getFirst(), force, velocityXZ.getSecond());
        }
    }

    @Inject(method = "explode", at = @At("HEAD"))
    private void preExplosion(CallbackInfo ci) {
        TntEntity tnt = ((TntEntity)(Object) this);
        World world = tnt.world;
        spawnTnt(world, tnt.getX(), tnt.getY(), tnt.getZ(), 1, 5);
    }

}
