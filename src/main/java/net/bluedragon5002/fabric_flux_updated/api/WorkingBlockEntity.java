package net.bluedragon5002.fabric_flux_updated.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Block entity working cycle template for energy-based machines
 */
public abstract class WorkingBlockEntity extends BlockEntity implements IFluxContainer {
    protected WorkState workState = WorkState.READY;
    protected Flux flux;
    protected int workNeeded, workDone;

    public WorkingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int cap, boolean receive) {
        super(type, pos, state);
        flux = new Flux(cap);
        flux.receive = receive;
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Flux", flux.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.flux.amount = nbt.getInt("Flux");
    }

    /**
     * Checks if Block entity can work.
     *
     * @return If {@code true} then work can be started or resumed.
     */
    protected abstract boolean canWork();

    /**
     * Checks if Block entity has work.
     *
     * @return If {@code true} then work can be resumed, otherwise new work will be started.
     * @see #canWork()
     */
    protected abstract boolean hasWork();

    /**
     * This is called when work is about to begin (Block entity can work now).
     *
     * @return Amount of work ticks.
     */
    protected abstract int beginWork();

    /**
     * This is called only when Block entity has work to do and has some energy left to use.
     *
     * @see #hasWork()
     * @see #canWork()
     */
    protected abstract void work();

    /**
     * This method is called when work is ending.
     */
    protected abstract void finishWork();

    /**
     * Occurs at every tick update.
     */
    protected abstract void tickAny();

    /**
     * Tick update hapenning on client-side.
     */
    protected abstract void tickClient();

    /**
     * Checks if energy value can change.
     *
     * @return {@code true} if energy can be changed, otherwise {@code false}.
     */
    protected abstract boolean canChangeFlux();


    public void tick(World world, BlockPos pos, BlockState state) {
        if (world != null && world.isClient) {
            tickClient();
            return;
        }
        tickAny();
        if (!canWork()) {
            workState = WorkState.READY;
        } else if (!hasWork()) {
            workDone = 0;
            workNeeded = beginWork();
            workState = WorkState.WORKING;
        } else if (canChangeFlux()) {
            work();
            ++workDone;
            if (workNeeded <= workDone) {
                finishWork();
                workState = WorkState.FINISHED;
            }
        }
    }

    @Override
    public Optional<IFlux> getFluxFor(Object that) {
        return Optional.of(flux);
    }

    public static final class Flux implements IFlux {
        boolean receive = false;
        int amount = 0;
        final int capacity;

        Flux(int cap) {
            capacity = cap;
        }

        @Override
        public int getFluxAmount() {
            return amount;
        }

        public void setFluxAmount(int flux) {
            amount = flux;
        }

        @Override
        public int getFluxCapacity() {
            return capacity;
        }

        @Override
        public int extractFlux(int n, boolean sim) {
            if (receive) return -1;
            if (n > 0) {
                if (n > amount)
                    n = amount;
                if (!sim)
                    amount -= n;
            }
            return n;
        }

        @Override
        public int receiveFlux(int n, boolean sim) {
            if (!receive) return -1;
            if (n > 0) {
                final int r = capacity - amount;
                if (n > r)
                    n = r;
                if (!sim)
                    amount += n;
            }
            return n;
        }
    }
}