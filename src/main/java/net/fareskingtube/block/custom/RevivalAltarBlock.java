package net.fareskingtube.block.custom;

import com.mojang.serialization.MapCodec;
import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.entity.custom.RevivalAltarBlockEntity;
import net.fareskingtube.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RevivalAltarBlock extends BlockWithEntity implements BlockEntityProvider {
    //    Yes. I know this is horrible code, but I can't find a better way to do it ¯\_(ツ)_/¯
    private static final VoxelShape ALTAR_BASE = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(1, 0, 1, 15, 1, 15),
            Block.createCuboidShape(2, 1, 2, 14, 2, 14),
            BooleanBiFunction.OR
    );

    private static final VoxelShape ALTAR_MIDDLE = VoxelShapes.combineAndSimplify(ALTAR_BASE,
            Block.createCuboidShape(4, 2, 4, 12, 10, 12),
            BooleanBiFunction.OR);

    private static final VoxelShape ALTAR_TOP = VoxelShapes.combineAndSimplify(ALTAR_MIDDLE,
            Block.createCuboidShape(1, 10, 1, 15, 12, 15),
            BooleanBiFunction.OR);

    private static final VoxelShape ALTAR_PLATES = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(3, 12, 3, 13, 12.75, 13),
            Block.createCuboidShape(5, 12.75, 5, 11, 13.25, 11),
            BooleanBiFunction.OR
    );

    private static final VoxelShape ALTAR_FULL_TOP = VoxelShapes.combineAndSimplify(
            ALTAR_TOP,
            ALTAR_PLATES,
            BooleanBiFunction.OR
    );


    private static final VoxelShape CORNER_NW = Block.createCuboidShape(0, 0, 0, 2, 13, 2);
    private static final VoxelShape CORNER_NE = Block.createCuboidShape(14, 0, 0, 16, 13, 2);
    private static final VoxelShape CORNER_SW = Block.createCuboidShape(14, 0, 14, 16, 13, 16);
    private static final VoxelShape CORNER_SE = Block.createCuboidShape(0, 0, 14, 2, 13, 16);

    private static final VoxelShape ALTAR_CORNERS = VoxelShapes.union(CORNER_NE, CORNER_NW, CORNER_SW, CORNER_SE);
    private static final VoxelShape ALTAR_CORNERS_HOLLOW = VoxelShapes.combineAndSimplify(ALTAR_CORNERS,
            Block.createCuboidShape(1, 10, 1, 15, 13, 15),
            BooleanBiFunction.ONLY_FIRST);

    public static final VoxelShape ALTAR_FINAL = VoxelShapes.combineAndSimplify(ALTAR_FULL_TOP, ALTAR_CORNERS_HOLLOW, BooleanBiFunction.OR);

    public static final MapCodec<RevivalAltarBlock> CODEC = RevivalAltarBlock.createCodec(RevivalAltarBlock::new);

    public RevivalAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RevivalAltarBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ALTAR_FINAL;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RevivalAltarBlockEntity) {
                ItemScatterer.spawn(world, pos, ((RevivalAltarBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof RevivalAltarBlockEntity revivalAltarBlockEntity) {
            if (hand.equals(Hand.OFF_HAND)) return ItemActionResult.FAIL;
            if (revivalAltarBlockEntity.isEmpty() && stack.isOf(ModItems.HARDCORE_HEART)) {
                if (!world.isClient()) {
                    revivalAltarBlockEntity.setStack(0, stack.copyWithCount(1));
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
                    stack.decrement(1);

                    revivalAltarBlockEntity.markDirty();
                }

                return ItemActionResult.SUCCESS;
            } else if (!revivalAltarBlockEntity.isEmpty() && stack.isEmpty() && !player.isSneaking()) {
                if (!world.isClient()) {
                    ItemStack stackOnRevivalAltar = revivalAltarBlockEntity.getStack(0);
                    player.setStackInHand(hand, stackOnRevivalAltar);
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                    revivalAltarBlockEntity.setStack(0, ItemStack.EMPTY);

                    revivalAltarBlockEntity.markDirty();
                }

                return ItemActionResult.SUCCESS;
            } else {
                return ItemActionResult.FAIL;
            }
        }
        return ItemActionResult.FAIL;
    }
}

