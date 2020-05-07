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

package com.terraformersmc.assembly.screen.builder.slot;


import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class FilteredSlot extends Slot {

	private Predicate<ItemStack> filter;
	private int stackLimit = 64;

	public FilteredSlot(final Inventory inventory, final int index, final int x, final int y) {
		super(inventory, index, x, y);
	}

	public FilteredSlot(final Inventory inventory, final int index, final int x, final int y, int stackLimit) {
		super(inventory, index, x, y);
		this.stackLimit = stackLimit;
	}

	public FilteredSlot setFilter(final Predicate<ItemStack> filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public boolean canInsert(final ItemStack stack) {
		try {
			return this.filter.test(stack);
		} catch (NullPointerException e) {
			return true;
		}
	}

	@Override
	public int getMaxStackAmount() {
		return this.stackLimit;
	}
}