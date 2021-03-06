package com.terraformersmc.assembly.blockentity;

import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.terraformersmc.assembly.blockentity.base.AssemblySyncedNbtBlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;
import com.terraformersmc.assembly.block.SpigotBlock;
import com.terraformersmc.assembly.util.AssemblyConstants;

public class SpigotBlockEntity extends AssemblySyncedNbtBlockEntity implements Tickable {

	private static final String TRANSFER_COOLDOWN_KEY = AssemblyConstants.NbtKeys.TRANSFER_COOLDOWN;
	private int transferCooldown;
	private long lastTickTime;

	private static final String POURING_FLUID_KEY = AssemblyConstants.NbtKeys.POURING_FLUID;
	private Fluid pouringFluid = Fluids.EMPTY;
	private Fluid lastPouringFluid = Fluids.EMPTY;

	public SpigotBlockEntity() {
		super(AssemblyBlockEntities.SPIGOT);
		this.transferCooldown = -1;
	}

	@Override
    public void fromTag(CompoundTag tag, boolean syncing) {
		this.transferCooldown = tag.getInt(TRANSFER_COOLDOWN_KEY);
		if (syncing) {
			this.pouringFluid = Registry.FLUID.get(new Identifier(tag.getString(POURING_FLUID_KEY)));
		}
	}

	@Override
    public CompoundTag toTag(CompoundTag tag, boolean syncing) {
		tag.putInt(TRANSFER_COOLDOWN_KEY, this.transferCooldown);
		if (syncing) {
			tag.putString(POURING_FLUID_KEY, Registry.FLUID.getId(this.pouringFluid).toString());
		}
		return tag;
	}

	@Override
    public void tick() {
		if (this.world != null && !this.world.isClient) {
			if (this.transferCooldown > 0) {
				--this.transferCooldown;
			}
			this.lastTickTime = this.world.getTime();
			if (!this.needsCooldown()) {
				if (this.world.getBlockState(this.pos).get(SpigotBlock.VALVE).isOpen()) {
					// Pull Fluid from fluid container it is attached to and push to the block below
					FluidVolume movedFluid = FluidVolumeUtil.move(FluidAttributes.EXTRACTABLE.get(this.world, this.pos.offset(this.getCachedState().get(SpigotBlock.FACING))), FluidAttributes.INSERTABLE.get(this.world, this.pos.down()), FluidAmount.BUCKET.roundedDiv(4));
					Fluid rawFluid = movedFluid.getRawFluid();
					this.pouringFluid = rawFluid == null ? Fluids.EMPTY : rawFluid;
					if (this.pouringFluid != this.lastPouringFluid) {
						this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(SpigotBlock.POURING, this.pouringFluid != Fluids.EMPTY));
                        this.sync();
					}
					if (!movedFluid.isEmpty()) {
						this.setCooldown(8);
					}
				} else {
					this.pouringFluid = Fluids.EMPTY;
					if (this.lastPouringFluid != this.pouringFluid) {
						this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(SpigotBlock.POURING, false));
					}
				}
				this.lastPouringFluid = this.pouringFluid;
			}

		}
	}

	private void setCooldown(int cooldown) {
		this.transferCooldown = cooldown;
	}

	private boolean needsCooldown() {
		return this.transferCooldown > 0;
	}

	private boolean isDisabled() {
		return this.transferCooldown > 8;
	}

	public Fluid getPouringFluid() {
		return this.pouringFluid;
	}
}
