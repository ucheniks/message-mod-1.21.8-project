package com.gshelgaas.messageserver.service;

import com.gshelgaas.messageserver.MessageMod;
import com.gshelgaas.messageserver.protobuf.MessageProto;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class MessageSenderServiceClient {
    private static final String SERVER_URL = "http://localhost:8080/api/messages";
    private final HttpClient httpClient;

    public MessageSenderServiceClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void sendMessage(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                throw new RuntimeException("Сообщение не может быть пустым");
            }

            if (text.length() > 256) {
                throw new RuntimeException("Сообщение превышает 256 символов");
            }

            UUID playerUuid = getPlayerUUID();
            MessageProto.Message protobufMessage = MessageProto.Message.newBuilder()
                    .setText(text.trim())
                    .build();

            byte[] messageData = protobufMessage.toByteArray();

            MessageMod.LOGGER.info("Отправка сообщения от игрока: {}", playerUuid);
            sendHttpRequest(playerUuid, messageData);
            MessageMod.LOGGER.info("Сообщение успешно отправлено: {}", text);

        } catch (Exception e) {
            MessageMod.LOGGER.error("Ошибка отправки сообщения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка отправки сообщения: " + e.getMessage(), e);
        }
    }

    private UUID getPlayerUUID() {
        if (Minecraft.getInstance().player != null) {
            return Minecraft.getInstance().player.getUUID();
        }
        throw new RuntimeException("Игрок не найден");
    }

    private void sendHttpRequest(UUID playerUuid, byte[] protobufData) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL))
                .header("Content-Type", "application/x-protobuf")
                .header("X-Player-UUID", playerUuid.toString())
                .POST(HttpRequest.BodyPublishers.ofByteArray(protobufData))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new RuntimeException("HTTP ошибка: " + response.statusCode() + " - " + response.body());
        }

        MessageMod.LOGGER.info("Сервер ответил: {}", response.statusCode());
    }
}