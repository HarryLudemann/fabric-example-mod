package com.hazzahs.mymod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// attaches a tick event handler, scans surrounding blocks for farmland or mature crops
// to plant on or harvest respectively
public class AutoFarmer {
    public static void register() {
        // Register a client tick event handler
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!ModClient.isAutoSeedEnabled && !ModClient.isAutoHarvestEnabled)
                return;

            // Get the player instance
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            if (minecraftClient.player == null)
                return;

            // Get the player's world
            World world = minecraftClient.world;
            if (world == null)
                return;

            // Define the offset range for the surrounding blocks
            int range = 3;

            // Get the player's current position
            BlockPos playerPos = minecraftClient.player.getBlockPos();

            // Iterate over the surrounding blocks
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = playerPos.add(x, y, z);
                        BlockState blockState = world.getBlockState(pos);
                        Block block = blockState.getBlock();
                        // if block Farmland/hoed ground
                        if (block.getName().getString().equals("Farmland")) {
                            handleAutoSeeding(pos, minecraftClient);
                        }
                        // If block is fully grown crop, or grass (grass that returns seeds)
                        else if (block instanceof CropBlock && ((CropBlock) block).isMature(blockState)
                                || block.getName().getString().equals("Grass")) {
                            handleHarvesting(pos, minecraftClient);
                        }
                    }
                }
            }
        });
    }

    // Handle auto seeding/planting functionality
    private static void handleAutoSeeding(BlockPos pos, MinecraftClient minecraftClient) {
        assert minecraftClient.player != null; // cant be null as register function would have already returned

        // if auto seed is disabled return
        if (!ModClient.isAutoSeedEnabled)
            return;

        // if player not holding seed in hand return
        if (!isHoldingSeed(minecraftClient))
            return;

        // if block above farmland is not air then return
        World world = minecraftClient.world;
        if (world == null)
            return;

        Block aboveBlock = world.getBlockState(pos.up()).getBlock();
        if (!aboveBlock.toString().equals("Block{minecraft:air}"))
            return;

        // Temporarily adjust the player's position closer to the crop
        Vec3d originalPlayerPos = minecraftClient.player.getPos();
        Vec3d adjustedPlayerPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        minecraftClient.player.setPos(adjustedPlayerPos.x, adjustedPlayerPos.y, adjustedPlayerPos.z);

        // Perform the interaction on the farmland block (plant seed)
        ClientPlayerInteractionManager interactionManager = minecraftClient.interactionManager;
        interactionManager.interactBlock(
                minecraftClient.player,
                minecraftClient.player.getActiveHand(),
                new BlockHitResult(adjustedPlayerPos, Direction.UP, pos, false));

        // Restore the player's original position
        minecraftClient.player.setPos(originalPlayerPos.x, originalPlayerPos.y, originalPlayerPos.z);

        ModClient.LOGGER.info("Planted Seed");
    }

    // Handle auto harvesting functionality
    private static void handleHarvesting(BlockPos pos, MinecraftClient minecraftClient) {
        // If auto harvest is disabled, return
        if (!ModClient.isAutoHarvestEnabled)
            return;
        assert minecraftClient.player != null;

        // Perform the interaction on the crop block (harvest crop)
        ClientPlayerInteractionManager interactionManager = minecraftClient.interactionManager;
        interactionManager.attackBlock(pos, Direction.UP); // Simulate block breaking

        ModClient.LOGGER.info("Harvested Crop");
    }

    // returns true if player is currently holding a seed item in hand
    private static boolean isHoldingSeed(MinecraftClient minecraftClient) {
        ItemStack itemStack = minecraftClient.player.getMainHandStack();
        if (itemStack.isOf(Items.WHEAT_SEEDS)
                || itemStack.isOf(Items.PUMPKIN_SEEDS)
                || itemStack.isOf(Items.MELON_SEEDS)
                || itemStack.isOf(Items.BEETROOT_SEEDS)) {
            return true;
        }
        return false;
    }
}
