package net.bluedragon5002.fabric_flux_updated.api;

import net.minecraft.item.ItemStack;

public class StackFlux extends Flux {

    private ItemStack stack;

    public StackFlux(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getFluxCapacity() {
        return this.stack.getMaxDamage();
    }

    @Override
    public void setFluxCapacity(int capacity) {
        // You shouldn't have to do this anyways
    }

    @Override
    public int getFluxAmount() {
        return this.getFluxCapacity() - this.stack.getDamage();
    }

    @Override
    public void setFluxAmount(int amount) {
        this.stack.setDamage(this.getFluxCapacity() - amount);
    }

}