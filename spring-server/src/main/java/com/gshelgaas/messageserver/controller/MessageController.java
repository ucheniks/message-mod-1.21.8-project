package com.gshelgaas.messageserver.controller;

import com.gshelgaas.messageserver.dto.MessageRequestDto;
import com.gshelgaas.messageserver.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = "application/x-protobuf")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(
            @RequestHeader("X-Player-UUID") UUID playerUuid,
            @RequestBody byte[] protobufData) {

        log.info("Получен Protobuf запрос от игрока {}", playerUuid);

        MessageRequestDto messageDto = MessageRequestDto.builder()
                .playerUuid(playerUuid)
                .protobufData(protobufData)
                .build();

        messageService.saveMessage(messageDto);

        log.info("Сообщение от игрока {} успешно обработано", playerUuid);
    }
}