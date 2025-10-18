package com.gshelgaas.messageserver.service;

import com.gshelgaas.messageserver.dto.MessageRequestDto;

public interface MessageService {
    void saveMessage(MessageRequestDto messageDto);
}