package com.gshelgaas.messageserver;

import com.gshelgaas.messageserver.gui.MessageScreenClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class MessageModClient implements ClientModInitializer {
    private static KeyMapping openGuiKey;
    private static boolean messageShown = false;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.messagemod.open_gui",
                GLFW.GLFW_KEY_M,
                "category.messagemod.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !messageShown) {
                client.player.displayClientMessage(
                        Component.literal("§6[MessageMod] §fНажми §aM §fдля отправки сообщений"),
                        false
                );
                messageShown = true;
            }

            if (openGuiKey.isDown()) {
                if (client.player != null) {
                    client.setScreen(new MessageScreenClient());
                }
            }
        });
    }
}