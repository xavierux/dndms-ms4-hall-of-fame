package com.xvclemente.dnd.ms4.service;

import com.xvclemente.dnd.dtos.events.AventuraFinalizadaEvent;
import com.xvclemente.dnd.dtos.events.ResultadoCombateIndividualEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j // Lombok para logging
public class RankingService {

    // Map<ID_Entidad (PJ o EN), Victorias>
    private final Map<String, Integer> victoriasPorId = new ConcurrentHashMap<>();
    // Map<ID_PJ, OroAcumulado>
    private final Map<String, Integer> oroPorPjId = new ConcurrentHashMap<>();

    public void procesarResultadoCombate(ResultadoCombateIndividualEvent event) {
        victoriasPorId.merge(event.getWinnerId(), 1, Integer::sum); // Suma 1 a las victorias del ganador
        log.info("MS4: Victoria registrada para {}. Total victorias: {}", event.getWinnerId(), victoriasPorId.get(event.getWinnerId()));
        // Opcional: registrar derrota para event.getLoserId() si quieres un ranking de derrotas.
    }

    public void procesarAventuraFinalizada(AventuraFinalizadaEvent event) {
        if ("PJs VICTORIOSOS".equalsIgnoreCase(event.getResultadoAventura()) && event.getPjsGanadoresIds() != null) {
            for (String pjId : event.getPjsGanadoresIds()) {
                oroPorPjId.merge(pjId, event.getOroGanadoPorPJ(), Integer::sum);
                log.info("MS4: PJ {} ganó {} oro. Total oro: {}", pjId, event.getOroGanadoPorPJ(), oroPorPjId.get(pjId));
            }
        }
    }

    public Map<String, Integer> getRankingVictorias(int limit) {
        return victoriasPorId.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // En caso de colisión (no debería pasar con IDs únicos)
                        LinkedHashMap::new // Para mantener el orden
                ));
    }

    public Map<String, Integer> getRankingOro(int limit) {
        return oroPorPjId.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}