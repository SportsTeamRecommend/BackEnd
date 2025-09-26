package org.example.backend.common.weight.service;

import org.example.backend.common.weight.entity.TeamWeight;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeightService {
    // 초안의 rankTeams()와 동일한 구조
    public List<Map.Entry<String, Double>> rankTeams(List<TeamWeight> teamWeights, double[] userPref) {
        Map<String, Double> scoreMap = new HashMap<>();

        for (TeamWeight teamWeight : teamWeights) {
            double sim = cosineSimilarity(userPref, teamWeight.toVector());
            scoreMap.put(teamWeight.getTeam().getTeamName(), sim); // 팀 이름
        }

        // 유사도 기준 내림차순 정렬 (초안과 동일)
        List<Map.Entry<String, Double>> rankedList = new ArrayList<>(scoreMap.entrySet());
        rankedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        return rankedList;
    }

    // 초안의 cosineSimilarity()와 동일
    public static double cosineSimilarity(double[] a, double[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
