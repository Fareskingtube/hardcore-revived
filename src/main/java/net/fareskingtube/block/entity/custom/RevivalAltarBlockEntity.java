package net.fareskingtube.block.entity.custom;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.entity.ImplementedInventory;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RevivalAltarBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private float rotation = 0;

    public RevivalAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REVIVAL_ALTAR_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public float getRenderingRotation(float rotationSpeedMultiplier) {
        rotation += 0.5f * rotationSpeedMultiplier;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (world != null) {
            world.markDirty(pos);

            if (!world.isClient) {
                world.updateListeners(
                        pos,
                        getCachedState(),
                        getCachedState(),
                        Block.NOTIFY_ALL
                );
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        inventory.set(0, ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
