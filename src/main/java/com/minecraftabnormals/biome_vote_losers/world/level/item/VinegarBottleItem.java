package com.minecraftabnormals.biome_vote_losers.world.level.item;

import com.minecraftabnormals.biome_vote_losers.recipe.recipes.ColorLoseRecipe;
import com.minecraftabnormals.biome_vote_losers.register.ModEntities;
import com.minecraftabnormals.biome_vote_losers.utils.RecipeUtils;
import com.minecraftabnormals.biome_vote_losers.world.level.entity.CalcitePowderReaction;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class VinegarBottleItem extends Item {
    public VinegarBottleItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        final var level = context.getLevel();
        final var pos   = context.getClickedPos();
        final var block = level.getBlockState(pos);


        /* Only allow using vinegar on the top face of the block. */
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.PASS;
        }


        if (block.is(Blocks.CALCITE)) {
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!level.isClientSide) {
                final var powderEntity = new CalcitePowderReaction(ModEntities.CALCITE_POWDER.get(), level);
                powderEntity.setPos(Vec3.upFromBottomCenterOf(pos, 1.0));

                level.addFreshEntity(powderEntity);
            }
        } else {
            ColorLoseRecipe recipe = RecipeUtils.blockColorLoose(level, pos, block);
            if (recipe != null) {
                BlockState state = recipe.getResultState(block);
                //check is using block same as result
                if (state.getBlock() != block.getBlock()) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);

                    level.setBlock(pos, state, 2);
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                return InteractionResult.PASS;
            }
        }

        final @Nullable var player = context.getPlayer();

        if (player != null && !player.getAbilities().instabuild) {
            final var item = context.getItemInHand();
            item.shrink(1);

            final var emptyBottle = new ItemStack(Items.GLASS_BOTTLE);

            if (item.isEmpty()) {
                player.setItemInHand(context.getHand(), emptyBottle);
            } else {
                player.addItem(emptyBottle);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
