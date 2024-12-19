package io.hhplus.tdd.point;

import io.hhplus.tdd.concurrency.UserLockProviderService;
import io.hhplus.tdd.exception.UserPointBadUsageException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointService {
    private static final int MAX_POINT_CHARGE = 10_000_000;

    private final UserLockProviderService userLockProviderService;

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    PointService(UserPointRepository userPointRepository,
                 PointHistoryRepository pointHistoryRepository,
                 UserLockProviderService userLockProviderService) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.userLockProviderService = userLockProviderService;
    }

    public UserPoint usePoint(Long userId, Long point) {
        if (point == 0) {
            throw new UserPointBadUsageException("사용할 포인트가 0입니다.");
        }
        if (point < 0) {
            throw new UserPointBadUsageException("사용할 포인트가 0보다 작습니다.");
        }

        userLockProviderService.lock(userId);
        try {
            UserPoint userPoint = userPointRepository.findByUserId(userId);
            if (userPoint.point() < point) {
                throw new UserPointBadUsageException("사용할 포인트가 보유한 포인트보다 많습니다.");
            }
            pointHistoryRepository.saveUserPointHistory(userId, TransactionType.USE, point);
            return userPointRepository.save(userId, userPoint.point() - point);
        } finally {
            userLockProviderService.unlock(userId);
        }
    }

    public UserPoint chargePoint(Long userId, Long point) {
        if (point == 0) {
            throw new UserPointBadUsageException("충전할 포인트가 0입니다.");
        }
        if (point < 0) {
            throw new UserPointBadUsageException("충전할 포인트가 0보다 작습니다.");
        }

        userLockProviderService.lock(userId);
        try {
            UserPoint userPoint = userPointRepository.findByUserId(userId);
            if (userPoint.point() + point > MAX_POINT_CHARGE) {
                throw new UserPointBadUsageException("최대 충전 가능한 포인트는 " + MAX_POINT_CHARGE + "입니다.");
            }
            pointHistoryRepository.saveUserPointHistory(userId, TransactionType.CHARGE, point);
            return userPointRepository.save(userId, userPoint.point() + point);
        }
        finally {
            userLockProviderService.unlock(userId);
        }
    }

    public UserPoint viewPoint(Long userId) {
        return userPointRepository.findByUserId(userId);
    }

    public List<PointHistory> viewPointHistory(Long userId){
        return pointHistoryRepository.getUserPointHistory(userId);
    }
}
