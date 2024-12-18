package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Component;

@Component
public class UserPointRepository {
    private final UserPointTable userPointTable;
    UserPointRepository(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint findByUserId(long userId) {
        return userPointTable.selectById(userId);
    }

    public void save(Long userId, Long point) {
        userPointTable.insertOrUpdate(userId, point);
    }
}
