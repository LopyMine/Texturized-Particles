package net.lopymine.tp.mixin;

//? >=1.21.2 {

/*import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.WorldEventHandler;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.tp.manager.TexturizedParticleManager;

import java.util.List;

@Mixin(WorldEventHandler.class)
public class WorldEventHandlerMixin {

	@Shadow @Final private World world;

	// SPLASH POTION
	@Inject(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;ofBottomCenter(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/math/Vec3d;"))
	private void modifyParticleEffect(int eventId, BlockPos pos, int data, CallbackInfo ci, @Share("tp_effects") LocalRef<List<ParticleEffect>> localParticleEffects) {
		TexturizedParticleManager.processSplashPotionStageOne(localParticleEffects, data);
	}

	// SPLASH POTION
	@WrapOperation(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;spawnParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)Lnet/minecraft/client/particle/Particle;", ordinal = 0))
	private Particle swapParticles(WorldRenderer instance, ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Particle> original, @Share("tp_effects") LocalRef<List<ParticleEffect>> localParticleEffects, @Local(argsOnly = true, ordinal = 1) int color) {
		return TexturizedParticleManager.processSplashPotionStageTwo(this.world, instance, parameters, alwaysSpawn, x, y, z, velocityX, velocityY, velocityZ, original, localParticleEffects, color);
	}
}

*///?}
