package org.example.backend.baseball.weight;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.common.weight.entity.WeightType;

// TODO : Setter 삭제 필요
@Getter @Setter
public class UserKboWeight {

    private WeightType recordPreference;         // 성적 선호
    private double recordImportance;             // 성적 중요도 (0~10)

    private WeightType legacyPreference;         // 근본 선호
    private double legacyImportance;             // 근본 중요도

    private WeightType franchiseStarPreference;  // 프스 선호
    private double franchiseStarImportance;      // 프스 중요도

    private WeightType growthPreference;         // 성장성 선호
    private double growthImportance;             // 성장성 중요도

    private WeightType regionPreference;         // 연고지 선호
    private double regionImportance;             // 연고지 중요도

    private WeightType fandomPreference;         // 팬덤 선호
    private double fandomImportance;             // 팬덤 중요도
}
