package com.gshelgaas.messageserver.repository;

import com.gshelgaas.messageserver.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}