package team.reborn.assembly.mixin.common.screenhandler;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ScreenHandler.class)
public interface AccessorScreenHandlerListeners {

	@Accessor
	List<ScreenHandlerListener> getListeners();
}
