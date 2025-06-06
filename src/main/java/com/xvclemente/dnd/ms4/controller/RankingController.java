package com.xvclemente.dnd.ms4.controller;

import com.xvclemente.dnd.ms4.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rankings")
public class RankingController {

    private final RankingService rankingService;

    @Autowired
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    /**
     * Endpoint para obtener el ranking de victorias.
     * @param limit Número máximo de entradas en el ranking a devolver (por defecto 10).
     * @return Un mapa con IDs de PJs/ENs y su número de victorias.
     */
    @GetMapping("/victories")
    public ResponseEntity<Map<String, Integer>> getVictoryRankings(@RequestParam(defaultValue = "10") int limit) {
        Map<String, Integer> rankings = rankingService.getRankingVictorias(limit);
        return ResponseEntity.ok(rankings);
    }

    /**
     * Endpoint para obtener el ranking de oro acumulado por PJs.
     * @param limit Número máximo de entradas en el ranking a devolver (por defecto 10).
     * @return Un mapa con IDs de PJs y su cantidad de oro.
     */
    @GetMapping("/gold")
    public ResponseEntity<Map<String, Integer>> getGoldRankings(@RequestParam(defaultValue = "10") int limit) {
        Map<String, Integer> rankings = rankingService.getRankingOro(limit);
        return ResponseEntity.ok(rankings);
    }
}