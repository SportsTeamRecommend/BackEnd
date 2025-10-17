package org.example.backend.common.weight.service;

import org.example.backend.baseball.team.Team;
import org.example.backend.common.weight.entity.KboTeamWeight;
import org.example.backend.common.weight.entity.UserKboWeight;
import org.example.backend.common.weight.entity.WeightType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class WeightServiceTest {

    private WeightService weightService;
    private List<KboTeamWeight> testTeamWeights;

    @BeforeEach
    void setUp() {
        weightService = new WeightService();
        testTeamWeights = createTestTeamWeights();
    }

    @Test
    @DisplayName("강팀 선호 사용자 테스트")
    void testStrongTeamPreference() {
        // given
        UserKboWeight user = createUser();
        user.setRecordPreference(WeightType.HIGH);
        user.setRecordImportance(9.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getKey()).isEqualTo("LG 트윈스");

        System.out.println("=== 강팀 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("약팀 선호 사용자 테스트")
    void testWeakTeamPreference() {
        // given
        UserKboWeight user = createUser();
        user.setRecordPreference(WeightType.LOW);
        user.setRecordImportance(8.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getKey()).isEqualTo("키움 히어로즈");

        System.out.println("=== 약팀 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("전통팀 선호 사용자 테스트")
    void testTraditionalTeamPreference() {
        // given
        UserKboWeight user = createUser();
        user.setLegacyPreference(WeightType.HIGH);
        user.setLegacyImportance(10.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();

        System.out.println("=== 전통팀 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("성장가능성 높은 팀 선호 사용자 테스트")
    void testYoungTeamPreference() {
        // given
        UserKboWeight user = createUser();
        user.setGrowthPreference(WeightType.HIGH);
        user.setGrowthImportance(8.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getKey()).isEqualTo("키움 히어로즈");

        System.out.println("=== 젊은팀 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("성장가능성 낮은 팀 선호 사용자 테스트")
    void testVeteranTeamPreference() {
        // given
        UserKboWeight user = createUser();
        user.setGrowthPreference(WeightType.LOW);
        user.setGrowthImportance(7.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getKey()).isEqualTo("KT 위즈");

        System.out.println("=== 베테랑팀 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("조용한 팬덤 선호 사용자 테스트")
    void testQuietFandomPreference() {
        // given
        UserKboWeight user = createUser();
        user.setFandomPreference(WeightType.LOW);
        user.setFandomImportance(9.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getKey()).isEqualTo("키움 히어로즈");

        System.out.println("=== 조용한 팬덤 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("강팀 + 작은팬덤 복합 선호도 테스트")
    void testStrongTeamSmallFandomPreference() {
        // given
        UserKboWeight user = createUser();
        user.setRecordPreference(WeightType.HIGH);
        user.setRecordImportance(8.0);
        user.setFandomPreference(WeightType.LOW);
        user.setFandomImportance(6.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();

        System.out.println("=== 강팀 + 작은팬덤 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("약팀 + 전통팀 복합 선호도 테스트")
    void testWeakTeamTraditionalPreference() {
        // given
        UserKboWeight user = createUser();
        user.setRecordPreference(WeightType.LOW);
        user.setRecordImportance(7.0);
        user.setLegacyPreference(WeightType.HIGH);
        user.setLegacyImportance(9.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();

        System.out.println("=== 약팀 + 전통팀 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("젊은팀 + 큰팬덤 복합 선호도 테스트")
    void testYoungTeamBigFandomPreference() {
        // given
        UserKboWeight user = createUser();
        user.setGrowthPreference(WeightType.HIGH);
        user.setGrowthImportance(6.0);
        user.setFandomPreference(WeightType.HIGH);
        user.setFandomImportance(8.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();

        System.out.println("=== 젊은팀 + 큰팬덤 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("복합 혼합 선호도 테스트 (강팀+신생+베테랑+작은팬덤)")
    void testComplexMixedPreference() {
        // given
        UserKboWeight user = createUser();
        user.setRecordPreference(WeightType.HIGH);
        user.setRecordImportance(6.0);
        user.setLegacyPreference(WeightType.LOW);
        user.setLegacyImportance(4.0);
        user.setGrowthPreference(WeightType.LOW);
        user.setGrowthImportance(5.0);
        user.setFandomPreference(WeightType.LOW);
        user.setFandomImportance(7.0);

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();

        System.out.println("=== 복합 혼합 선호 결과 ===");
        printResult(result);
    }

    @Test
    @DisplayName("모든 선호도 NONE인 경우")
    void testAllNonePreference() {
        // given
        UserKboWeight user = createUser(); // 모든 값이 NONE

        // when
        List<Map.Entry<String, Double>> result = weightService.kboRankTeams(testTeamWeights, user);

        // then
        assertThat(result).isNotEmpty();
        // 모든 팀의 점수가 0이어야 함
        assertThat(result.get(0).getValue()).isEqualTo(0.0);

        System.out.println("=== 모든 선호도 NONE 결과 ===");
        printResult(result);
    }

    private UserKboWeight createUser() {
        UserKboWeight user = new UserKboWeight();
        // 모든 값을 NONE으로 초기화
        user.setRecordPreference(WeightType.NONE);
        user.setRecordImportance(0.0);
        user.setLegacyPreference(WeightType.NONE);
        user.setLegacyImportance(0.0);
        user.setFranchiseStarPreference(WeightType.NONE);
        user.setFranchiseStarImportance(0.0);
        user.setGrowthPreference(WeightType.NONE);
        user.setGrowthImportance(0.0);
        user.setRegionPreference(WeightType.NONE);
        user.setRegionImportance(0.0);
        user.setFandomPreference(WeightType.NONE);
        user.setFandomImportance(0.0);
        return user;
    }

    private List<KboTeamWeight> createTestTeamWeights() {
        Team lgTeam = new Team("LG", "LG 트윈스");
        Team ssgTeam = new Team("SK", "SSG 랜더스");
        Team samsungTeam = new Team("SS", "삼성 라이온즈");
        Team kiaTeam = new Team("HT", "KIA 타이거즈");
        Team doosanTeam = new Team("OB", "두산 베어스");
        Team ktTeam = new Team("KT", "KT 위즈");
        Team hanwhaTeam = new Team("HH", "한화 이글스");
        Team lotteTeam = new Team("LT", "롯데 자이언츠");
        Team ncTeam = new Team("NC", "NC 다이노스");
        Team kiwoomTeam = new Team("WO", "키움 히어로즈");

        return Arrays.asList(
                new KboTeamWeight(1L, lgTeam, 9.33, 10.0, 0.0, 3.78, 0.0, 6.57),      // LG 트윈스
                new KboTeamWeight(2L, ssgTeam, 7.0, 5.7, 0.0, 2.05, 0.0, 3.31),       // SSG 랜더스
                new KboTeamWeight(3L, samsungTeam, 6.33, 10.0, 0.0, 3.42, 0.0, 7.53), // 삼성 라이온즈
                new KboTeamWeight(4L, kiaTeam, 6.0, 10.0, 0.0, 4.02, 0.0, 10.0),       // KIA 타이거즈
                new KboTeamWeight(5L, doosanTeam, 5.0, 10.0, 0.0, 4.49, 0.0, 4.88),    // 두산 베어스
                new KboTeamWeight(6L, ktTeam, 7.0, 2.6, 0.0, 1.0, 0.0, 1.22),          // KT 위즈
                new KboTeamWeight(7L, hanwhaTeam, 4.7, 9.0, 0.0, 2.43, 0.0, 8.30),     // 한화 이글스
                new KboTeamWeight(8L, lotteTeam, 4.33, 10.0, 0.0, 6.16, 0.0, 9.71),    // 롯데 자이언츠
                new KboTeamWeight(9L, ncTeam, 4.33, 2.9, 0.0, 4.57, 0.0, 1.48),        // NC 다이노스
                new KboTeamWeight(10L, kiwoomTeam, 1.0, 3.8, 0.0, 10.0, 0.0, 1.0)     // 키움 히어로즈
        );
    }

    private void printResult(List<Map.Entry<String, Double>> result) {
        for (int i = 0; i < result.size(); i++) {
            Map.Entry<String, Double> entry = result.get(i);
            System.out.printf("%d위: %s (점수: %.2f)\n\n", i + 1, entry.getKey(), entry.getValue() * 100);
        }
    }
}