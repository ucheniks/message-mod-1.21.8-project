package com.gshelgaas.messageserver.gui;

import com.gshelgaas.messageserver.MessageMod;
import com.gshelgaas.messageserver.service.MessageSenderServiceClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class MessageScreenClient extends Screen {
    private EditBox messageField;
    private Button sendButton;

    public MessageScreenClient() {
        super(Component.literal("Отправка сообщения"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.messageField = new EditBox(
                this.font,
                centerX - 150, centerY - 30,
                300, 20,
                Component.literal("Введите сообщение")
        );
        this.messageField.setMaxLength(256);
        this.messageField.setHint(Component.literal("Напишите ваше сообщение здесь..."));
        this.addRenderableWidget(this.messageField);

        this.sendButton = Button.builder(
                        Component.literal("Отправить"),
                        button -> this.onSendButtonPressed()
                )
                .bounds(centerX - 75, centerY + 10, 150, 20)
                .build();
        this.addRenderableWidget(this.sendButton);

        this.setInitialFocus(this.messageField);
    }

    private void onSendButtonPressed() {
        String messageText = this.messageField.getValue().trim();

        if (messageText.isEmpty()) {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                        Component.literal("§c[MessageMod] §fСообщение не может быть пустым"),
                        false
                );
            }
            return;
        }

        this.sendMessageToServer(messageText);
    }

    private void sendMessageToServer(String message) {
        try {
            MessageSenderServiceClient senderService = new MessageSenderServiceClient();
            senderService.sendMessage(message);

            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                        Component.literal("§a[MessageMod] §fСообщение отправлено: §e" + message),
                        false
                );
            }

            MessageMod.LOGGER.info("Сообщение отправлено: {}", message);

        } catch (Exception e) {
            if (this.minecraft != null && this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                        Component.literal("§c[MessageMod] §f" + e.getMessage()),
                        false
                );
            }
            MessageMod.LOGGER.warn("Ошибка отправки: {}", e.getMessage());
        } finally {
            if (this.minecraft != null) {
                this.minecraft.setScreen(null);
            }
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        context.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                this.height / 2 - 60,
                0xFFFFFF
        );

        context.drawCenteredString(
                this.font,
                Component.literal("Сообщение будет сохранено в базе данных"),
                this.width / 2,
                this.height / 2 - 45,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);
    }
}