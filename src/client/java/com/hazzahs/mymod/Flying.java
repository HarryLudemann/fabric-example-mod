package com.hazzahs.mymod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Flying {
    private static final double MAX_SPEED = 1.0; // Adjust the maximum speed as needed
    private static final double FALL_SPEED = -0.04;
    private static double acceleration = 0.1;
    private static int tickCount = 0;

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (ModClient.isFlyingEnabled) {
                tick(MinecraftClient.getInstance());
            }
        });
    }

    private static void tick(MinecraftClient minecraftClient) {
        boolean jumpPressed = minecraftClient.options.jumpKey.isPressed();
        boolean forwardPressed = minecraftClient.options.forwardKey.isPressed();
        boolean backPressed = minecraftClient.options.backKey.isPressed();
        boolean leftPressed = minecraftClient.options.leftKey.isPressed();
        boolean rightPressed = minecraftClient.options.rightKey.isPressed();

        Entity object = minecraftClient.player;
        if (object == null) {
            return;
        }

        if (minecraftClient.player.hasVehicle()) {
            object = minecraftClient.player.getVehicle();
        }

        Vec3d velocity = object.getVelocity();
        Vec3d newVelocity = new Vec3d(velocity.x, -FALL_SPEED, velocity.z);

        if (jumpPressed) {
            Vec3d movementVector = new Vec3d(0, 0, 0);
            if (forwardPressed || backPressed || leftPressed || rightPressed) {
                movementVector = movementVector.add(object.getRotationVector().multiply(acceleration));
                if (forwardPressed) {
                    movementVector = movementVector.add(object.getRotationVector().multiply(acceleration));
                }
                if (backPressed) {
                    movementVector = movementVector.subtract(object.getRotationVector().multiply(acceleration));
                }
                if (leftPressed && !minecraftClient.player.hasVehicle()) {
                    movementVector = movementVector.add(object.getRotationVector().rotateY((float) Math.PI / 2).multiply(acceleration));
                }
                if (rightPressed && !minecraftClient.player.hasVehicle()) {
                    movementVector = movementVector.add(object.getRotationVector().rotateY((float) -Math.PI / 2).multiply(acceleration));
                }
            }

            Vec3d normalizedVelocity = newVelocity.normalize();
            newVelocity = normalizedVelocity.multiply(acceleration).add(movementVector);

            double currentSpeed = Math.sqrt(newVelocity.x * newVelocity.x + newVelocity.z * newVelocity.z);
            if (currentSpeed > MAX_SPEED) {
                newVelocity = newVelocity.multiply(MAX_SPEED / currentSpeed);
            }

            newVelocity = new Vec3d(newVelocity.x, (tickCount == 0 && newVelocity.y > FALL_SPEED) ? FALL_SPEED : newVelocity.y, newVelocity.z);
            object.setVelocity(newVelocity);
        if (acceleration < MAX_SPEED) {
            acceleration += 0.1;
        }
        } else {
            if (acceleration > 0.2) {
                acceleration -= 0.2;
            }
        }

        if (tickCount == 0 || newVelocity.y <= FALL_SPEED) {
            tickCount = 30;
        }
        tickCount--;
    }
}


