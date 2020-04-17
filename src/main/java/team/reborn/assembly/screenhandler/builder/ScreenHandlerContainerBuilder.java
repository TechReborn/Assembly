/*
 * Copyright (c) 2018 modmuss50 and Gigabit101
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package team.reborn.assembly.screenhandler.builder;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import team.reborn.assembly.screenhandler.builder.slot.FilteredSlot;
import team.reborn.assembly.screenhandler.builder.slot.OutputSlot;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ScreenHandlerContainerBuilder {

	private final Inventory container;
	private final ScreenHandlerBuilder parent;
	private final int rangeStart;

	ScreenHandlerContainerBuilder(final ScreenHandlerBuilder parent, final Inventory tile) {
		this.container = tile;
		this.parent = parent;
		this.rangeStart = parent.slots.size();
	}

	public ScreenHandlerContainerBuilder slot(final int index, final int x, final int y) {
		this.parent.slots.add(new Slot(this.container, index, x, y));
		return this;
	}

	public ScreenHandlerContainerBuilder outputSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new OutputSlot(this.container, index, x, y));
		return this;
	}

	public ScreenHandlerContainerBuilder filterSlot(final int index, final int x, final int y,
													final Predicate<ItemStack> filter) {
		this.parent.slots.add(new FilteredSlot(this.container, index, x, y).setFilter(filter));
		return this;
	}

	/**
	 * @param supplier The supplier it can supply a variable holding in an Object it
	 *                 will be synced with a custom packet
	 * @param setter   The setter to call when the variable has been updated.
	 * @return ContainerTileInventoryBuilder InventoryBaseBlockEntity which will do the sync
	 */
	public <T> ScreenHandlerContainerBuilder sync(final Supplier<T> supplier, final Consumer<T> setter) {
		this.parent.objectValues.add(Pair.of(supplier, setter));
		return this;
	}

	public ScreenHandlerContainerBuilder onCraft(final Consumer<CraftingInventory> onCraft) {
		this.parent.craftEvents.add(onCraft);
		return this;
	}

	public ScreenHandlerBuilder addContainer() {
		this.parent.blockEntityRange.add(Range.between(this.rangeStart, this.parent.slots.size() - 1));
		return this.parent;
	}

	public ScreenHandlerContainerBuilder syncCrafterValue() {
		return this;
	}
}