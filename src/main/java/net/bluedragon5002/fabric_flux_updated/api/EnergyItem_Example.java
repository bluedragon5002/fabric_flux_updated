package net.bluedragon5002.fabric_flux_updated.api;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class EnergyItem_Example extends Item implements IFluxContainer {

    public EnergyItem_Example(Settings settings) {
        this(Flux.BASE_FLUX, settings);
    }

    public EnergyItem_Example(int fluxCapacity, Settings settings) {
        super(settings.maxDamage(fluxCapacity).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        stack.setDamage(stack.getMaxDamage() - (stack.getMaxDamage()-1));
        super.onCraft(stack, world, player);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {

        if (this.isIn(group)) {

            // Empty Stack
            ItemStack damagedStack = new ItemStack(this);
            damagedStack.setDamage(getMaxDamage());
            stacks.add(damagedStack);

            // Full stack
            stacks.add(new ItemStack(this));

        }

    }

    /*
     * Aesthetics
     */

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {

        // Energy amount
        getFluxFor(stack).ifPresent((flux) -> tooltip.add(new LiteralText("§9Energy: " + flux.getFluxAmount() + flux.getFluxCapacity() + "BDE§r")));

        super.appendTooltip(stack, world, tooltip, context);

    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    /*
     * Fabric Flux
     */

    public Flux getFlux(ItemStack stack) {
        return new StackFlux(stack);
    }

    @Override
    public Optional<IFlux> getFluxFor(Object object) {
        if (object instanceof ItemStack && ((ItemStack) object).getItem() instanceof EnergyItem_Example) return Optional.of(getFlux((ItemStack) object));
        else return Optional.empty();
    }

}