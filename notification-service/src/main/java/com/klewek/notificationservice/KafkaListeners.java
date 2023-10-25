package com.klewek.notificationservice;

import com.klewek.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListeners {

    @KafkaListener(topics = "notificationTopic", groupId = "notificationGroupId")
    public void notificationTopicListener(OrderPlacedEvent orderPlacedEvent){
        log.info("Listener received order: {}", orderPlacedEvent.orderNumber());
    }
}
