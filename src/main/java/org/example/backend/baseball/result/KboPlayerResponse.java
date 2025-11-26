package org.example.backend.baseball.result;

import org.example.backend.baseball.table.KboPlayerInfo;

public record KboPlayerResponse(
        int id,
        String name,
        String nationality,
        String debutYear,
        String dateOfBirth
) {
    public KboPlayerResponse(KboPlayerInfo kboPlayerInfo) {
        this(
                kboPlayerInfo.getId(),
                kboPlayerInfo.getName(),
                kboPlayerInfo.getNationality(),
                kboPlayerInfo.getDebutYear(),
                kboPlayerInfo.getDateOfBirth()
        );
    }
}
