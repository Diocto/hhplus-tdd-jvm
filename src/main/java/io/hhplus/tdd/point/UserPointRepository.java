package io.hhplus.tdd.point;

import org.springframework.stereotype.Component;

@Component
public class UserPointRepository {
    public UserPoint findByUserId(long userId) {
        return UserPoint.empty(userId);
    }

    public void save(UserPoint userPoint) {
    }
}
