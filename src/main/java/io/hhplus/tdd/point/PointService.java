package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.UserPointBadUsageException;
import org.springframework.stereotype.Component;

@Component
public class PointService {
    private static final int MAX_POINT_CHARGE = 10_000_000;

    private final UserPointRepository userPointRepository;

    PointService(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }

    public void usePoint(int userId, int point) {
        if (point == 0) {
            throw new UserPointBadUsageException("사용할 포인트가 0입니다.");
        }
        if (point < 0) {
            throw new UserPointBadUsageException("사용할 포인트가 0보다 작습니다.");
        }
        UserPoint userPoint = userPointRepository.findByUserId(userId);
        if (userPoint.point() < point) {
            throw new UserPointBadUsageException("사용할 포인트가 보유한 포인트보다 많습니다.");
        }

    }

    public void chargePoint(int userId, int point) {
        if (point == 0) {
            throw new UserPointBadUsageException("충전할 포인트가 0입니다.");
        }
        if (point < 0) {
            throw new UserPointBadUsageException("충전할 포인트가 0보다 작습니다.");
        }

        UserPoint userPoint = userPointRepository.findByUserId(userId);
        if (userPoint.point() + point > MAX_POINT_CHARGE) {
            throw new UserPointBadUsageException("최대 충전 가능한 포인트는 " + MAX_POINT_CHARGE + "입니다.");
        }
    }

    public UserPoint viewPoint(int userId) {
        return userPointRepository.findByUserId(userId);
    }
}
