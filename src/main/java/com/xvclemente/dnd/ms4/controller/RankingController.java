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

    @GetMapping("/victories")
    public ResponseEntity<Map<String, Integer>> getVictoryRankings(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(rankingService.getRankingVictorias(limit));
    }

    @GetMapping("/gold")
    public ResponseEntity<Map<String, Integer>> getGoldRankings(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(rankingService.getRankingOro(limit));
    }
}