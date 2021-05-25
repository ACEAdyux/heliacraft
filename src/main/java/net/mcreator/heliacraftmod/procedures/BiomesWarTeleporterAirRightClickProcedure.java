package net.mcreator.heliacraftmod.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.RegistryKey;
import net.minecraft.potion.EffectInstance;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import net.mcreator.heliacraftmod.item.BiomesWarTeleporterItem;
import net.mcreator.heliacraftmod.HeliacraftModModElements;
import net.mcreator.heliacraftmod.HeliacraftModMod;

import java.util.Map;
import java.util.Collections;

@HeliacraftModModElements.ModElement.Tag
public class BiomesWarTeleporterAirRightClickProcedure extends HeliacraftModModElements.ModElement {
	public BiomesWarTeleporterAirRightClickProcedure(HeliacraftModModElements instance) {
		super(instance, 3);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				HeliacraftModMod.LOGGER.warn("Failed to load dependency entity for procedure BiomesWarTeleporterAirRightClick!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		{
			Entity _ent = entity;
			if (!_ent.world.isRemote && _ent instanceof ServerPlayerEntity) {
				RegistryKey<World> destinationType = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,
						new ResourceLocation("heliacraft_mod:biomes_war"));
				ServerWorld nextWorld = _ent.getServer().getWorld(destinationType);
				if (nextWorld != null) {
					((ServerPlayerEntity) _ent).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241768_e_, 0));
					((ServerPlayerEntity) _ent).teleport(nextWorld, nextWorld.getSpawnPoint().getX(), nextWorld.getSpawnPoint().getY() + 1,
							nextWorld.getSpawnPoint().getZ(), _ent.rotationYaw, _ent.rotationPitch);
					((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) _ent).abilities));
					for (EffectInstance effectinstance : ((ServerPlayerEntity) _ent).getActivePotionEffects()) {
						((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayEntityEffectPacket(_ent.getEntityId(), effectinstance));
					}
					((ServerPlayerEntity) _ent).connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		}
		if (entity instanceof PlayerEntity) {
			ItemStack _stktoremove = new ItemStack(BiomesWarTeleporterItem.block, (int) (1));
			((PlayerEntity) entity).inventory.func_234564_a_(p -> _stktoremove.getItem() == p.getItem(), (int) 1,
					((PlayerEntity) entity).container.func_234641_j_());
		}
		{
			Entity _ent = entity;
			_ent.setPositionAndUpdate(8, 64, 15);
			if (_ent instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) _ent).connection.setPlayerLocation(8, 64, 15, _ent.rotationYaw, _ent.rotationPitch, Collections.emptySet());
			}
		}
	}
}
