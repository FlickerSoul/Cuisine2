package snownee.cuisine.processing.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import snownee.kiwi.tile.BaseTile;

public class MillTile extends BaseTile {

    public MillTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    protected void readPacketData(CompoundNBT data) {
        // TODO Auto-generated method stub

    }

    @Override
    protected CompoundNBT writePacketData(CompoundNBT data) {
        // TODO Auto-generated method stub
        return data;
    }

}
