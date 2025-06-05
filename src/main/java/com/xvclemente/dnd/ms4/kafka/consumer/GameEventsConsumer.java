package com.xvclemente.dnd.ms4.kafka.consumer;

import com.xvclemente.dnd.dtos.events.AventuraFinalizadaEvent;
import com.xvclemente.dnd.dtos.events.ResultadoCombateIndividualEvent;
import com.xvclemente.dnd.ms4.service.RankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GameEventsConsumer {

    private final RankingService rankingService;

    @Autowired
    public GameEventsConsumer(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @KafkaListener(topics = "${app.kafka.topic.combate-resultados}",
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "resultadoCombateIndividualEventKafkaListenerContainerFactory")
    public void handleResultadoCombateIndividual(@Payload ResultadoCombateIndividualEvent event) {
        log.info("MS4: ResultadoCombateIndividualEvent recibido para adventureId: {}, Ganador: {} ({})",
                event.getAdventureId(), event.getWinnerId(), event.getWinnerType());
        rankingService.procesarResultadoCombate(event);
    }

    @KafkaListener(topics = "${app.kafka.topic.aventura-finalizada}",
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "aventuraFinalizadaEventKafkaListenerContainerFactory")
    public void handleAventuraFinalizada(@Payload AventuraFinalizadaEvent event) {
        log.info("MS4: AventuraFinalizadaEvent recibido para adventureId: {}, Resultado: {}",
                event.getAdventureId(), event.getResultadoAventura());
        rankingService.procesarAventuraFinalizada(event);
    }
}