package net.bluedragon5002.fabric_flux_updated.api;

import net.minecraft.nbt.NbtCompound;

public class Flux implements IFlux {

    public static final int BASE_FLUX = 65536;

    // Use PascalCase for tags, just like the base game
    public static final String CAPACITY_TAG_STR = "FluxCapacity";
    public static final String AMOUNT_TAG_STR = "FluxAmount";

    private int capacity;
    private int amount;

    public Flux() {
        this(BASE_FLUX, 0);
    }

    public Flux(int capacity) {
        this(capacity, 0);
    }

    public Flux(int capacity, int amount) {
        this.capacity = capacity;
        this.amount = amount;
    }

    @Override
    public int getFluxCapacity() {
        return this.capacity;
    }

    public void setFluxCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getFluxAmount() {
        return this.amount;
    }

    public void setFluxAmount(int amount) {
        this.amount = amount;
    }

    public int extractFlux(int n) {
        return this.extractFlux(n, false);
    }

    @Override
    public int extractFlux(int n, boolean sim) {
        int amt = (getFluxAmount() - n >= 0) ? n : getFluxAmount();
        if (!sim) this.setFluxAmount(getFluxAmount() - amt);
        return amt;
    }

    public int receiveFlux(int n) {
        return this.receiveFlux(n, false);
    }

    @Override
    public int receiveFlux(int n, boolean sim) {
        int amt = Math.min(getFluxCapacity() - getFluxAmount(), n);
        if (!sim) this.setFluxAmount(getFluxAmount() + amt);
        return amt;
    }

    @Override
    public int to(IFlux dest, int amount) {
        return IFlux.super.to(dest, Math.min(amount, dest.getFluxCapacity() - dest.getFluxAmount()));
    }

    public NbtCompound toTag(NbtCompound tag) {

        tag.putInt(CAPACITY_TAG_STR, getFluxCapacity());
        tag.putInt(AMOUNT_TAG_STR, getFluxAmount());

        return tag;

    }

    public void fromTag(NbtCompound tag) {

        this.setFluxCapacity(tag.getInt(CAPACITY_TAG_STR));
        this.setFluxAmount(tag.getInt(AMOUNT_TAG_STR));

    }

}
