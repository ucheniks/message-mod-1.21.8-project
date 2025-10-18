package com.gshelgaas.messageserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
    private byte[] protobufData;
    private UUID playerUuid;
}