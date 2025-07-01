package com.xvclemente.dnd.ms4.service;

import com.xvclemente.dnd.dtos.events.AventuraFinalizadaEvent;
import com.xvclemente.dnd.dtos.events.ResultadoCombateIndividualEvent;
import com.xvclemente.dnd.ms4.model.RankingEntry;
import com.xvclemente.dnd.ms4.repository.RankingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RankingService {

    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public void procesarResultadoCombate(ResultadoCombateIndividualEvent event) {
        // Obtiene la entrada actual del ranking, o crea una nueva si no existe
        RankingEntry entry = rankingRepository.findById(event.getWinnerId())
                .orElse(new RankingEntry(event.getWinnerId()));

        // Actualiza y guarda
        entry.setVictories(entry.getVictories() + 1);
        rankingRepository.save(entry);
        log.info("MS4: Victoria registrada para {}. Total victorias: {}", entry.getEntityId(), entry.getVictories());
    }

    public void procesarAventuraFinalizada(AventuraFinalizadaEvent event) {
        if ("PJs VICTORIOSOS".equalsIgnoreCase(event.getResultadoAventura()) && event.getPjsGanadoresIds() != null) {
            for (String pjId : event.getPjsGanadoresIds()) {
                RankingEntry entry = rankingRepository.findById(pjId)
                        .orElse(new RankingEntry(pjId));

                entry.setGold(entry.getGold() + event.getOroGanadoPorPJ());
                rankingRepository.save(entry);
                log.info("MS4: PJ {} ganó {} oro. Total oro: {}", entry.getEntityId(), event.getOroGanadoPorPJ(), entry.getGold());
            }
        }
    }

    public Map<String, Integer> getRankingVictorias(int limit) {
        return rankingRepository.findAll().stream()
                .filter(entry -> entry.getVictories() > 0)
                .sorted(Comparator.comparingInt(RankingEntry::getVictories).reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        RankingEntry::getEntityId,
                        RankingEntry::getVictories,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Integer> getRankingOro(int limit) {
        return rankingRepository.findAll().stream()
                .filter(entry -> entry.getGold() > 0)
                .sorted(Comparator.comparingInt(RankingEntry::getGold).reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        RankingEntry::getEntityId,
                        RankingEntry::getGold,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}