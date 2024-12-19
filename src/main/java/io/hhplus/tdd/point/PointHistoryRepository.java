package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PointHistoryRepository {
    private final PointHistoryTable pointHistoryTable;

    PointHistoryRepository(PointHistoryTable pointHistoryTable){
        this.pointHistoryTable = pointHistoryTable;
    }

    List<PointHistory> getUserPointHistory(Long userId){
        return new ArrayList<>();
    }

    void saveUserPointHistory(Long userId, TransactionType type, Long point){
        return;
    }
}
