package net.bluedragon5002.fabric_flux_updated.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

import java.util.HashMap;

public enum FluxIOState {

    DISABLED(0, 8355711, (Text) new LiteralText("DISABLED")),
    INPUT(1, 1153535, (Text) new LiteralText("INPUT")),
    OUTPUT(2, 16750865, (Text) new LiteralText("OUTPUT")),
    INPUT_OUTPUT(3, 7838071, (Text) new LiteralText("INPUT_OUTPUT"));

    public int id;
    public int color;
    public Text lang;

    FluxIOState(int id, int color, Text lang) {
        this.id = id;
        this.color = color;
        this.lang = lang;
    }

    public static NbtCompound toTag(NbtCompound tag, HashMap<Direction, FluxIOState> io) {

        for (Direction d : io.keySet()) tag.putInt("FluxIOState." + d.asString(), io.get(d).id);

        return tag;

    }

    public static HashMap<Direction, FluxIOState> fromTag(NbtCompound tag) {

        HashMap<Direction, FluxIOState> io = new HashMap<>();

        for (Direction d : Direction.values()) io.put(d, FluxIOState.values()[tag.getInt("FluxIOState." + d.asString())]);

        return io;

    }

}