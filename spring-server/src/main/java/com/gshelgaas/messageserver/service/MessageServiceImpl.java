package com.gshelgaas.messageserver.service;

import com.gshelgaas.messageserver.dto.MessageRequestDto;
import com.gshelgaas.messageserver.entity.MessageEntity;
import com.gshelgaas.messageserver.protobuf.MessageProto;
import com.gshelgaas.messageserver.exception.ProtobufProcessingException;
import com.gshelgaas.messageserver.repository.MessageRepository;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public void saveMessage(MessageRequestDto messageDto) {
        try {
            MessageProto.Message message = MessageProto.Message.parseFrom(messageDto.getProtobufData());
            String text = message.getText();

            if (text.length() > 256) {
                throw new ProtobufProcessingException("Текст сообщения превышает 256 символов: " + text.length());
            }

            MessageEntity entity = MessageEntity.builder()
                    .uuid(messageDto.getPlayerUuid())
                    .text(text)
                    .build();

            MessageEntity saved = messageRepository.save(entity);

            log.info("Сообщение сохранено с ID: {}, длина текста: {} символов", saved.getId(), text.length());

        } catch (InvalidProtocolBufferException e) {
            log.error("Ошибка декодирования Protobuf от {}: {}", messageDto.getPlayerUuid(), e.getMessage());
            throw new ProtobufProcessingException("Неверный формат Protobuf сообщения", e);
        } catch (Exception e) {
            log.error("Ошибка сохранения Protobuf сообщения от {}: {}", messageDto.getPlayerUuid(), e.getMessage(), e);
            throw new ProtobufProcessingException("Ошибка обработки сообщения", e);
        }
    }
}