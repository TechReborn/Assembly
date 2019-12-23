package team.reborn.assembly.container;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import team.reborn.assembly.blockentity.BoilerBlockEntity;
import team.reborn.assembly.client.gui.BaseScreen;
import team.reborn.assembly.container.builder.BuiltMenu;

public class AssemblyContainers {

	public static final Identifier BOILER = new Identifier("assembly", "grinder");

	public static void register() {
		ContainerProviderRegistry.INSTANCE.registerFactory(BOILER, (syncId, id, player, buf) -> {
			BlockPos pos = buf.readBlockPos();
			BlockEntity blockEntity = player.world.getBlockEntity(pos);
			if (blockEntity instanceof BoilerBlockEntity) {
				return ((BoilerBlockEntity) blockEntity).createContainer(syncId, player.inventory);
			}
			return null;
		});
		ScreenProviderRegistry.INSTANCE.registerFactory(BOILER, container -> {
			if (container instanceof BuiltMenu) {
				return new BaseScreen((BuiltMenu) container, "hi");
			}
			return null;
		});

	}

}