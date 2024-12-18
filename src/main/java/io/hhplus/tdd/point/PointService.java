package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.UserPointBadUsageException;
import org.springframework.stereotype.Component;

@Component
public class PointService {

    public void usePoint(int userId, int point) {
        if (point == 0) {
            throw new UserPointBadUsageException("사용할 포인트가 0입니다.");
        }
        if (point < 0) {
            throw new UserPointBadUsageException("사용할 포인트가 0보다 작습니다.");
        }
    }
}
