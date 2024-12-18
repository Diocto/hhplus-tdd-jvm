package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.UserPointBadUsageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PointServiceTest {
    // 서비스 테스트케이스
    // 1. 실패하는 케이스부터 구현할 것
    // 2. 예외 케이스부터 구현할 것
    // 3. 성공하는 케이스를 구현할 것

    @Mock
    private UserPointRepository userPointRepositoryMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
    }

    @Test
    void useZeroPoint() {
        // 1. 포인트 사용
        // 1.1 사용을 실패하는 경우 - 사용할 포인트가 0
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> pointService.usePoint(1, 0));
    }

    @Test
    void useNegativePoint() {
        // 1. 포인트 사용
        // 1.4 사용할 포인트가 0보다 작은 경우
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> pointService.usePoint(1, -1));
    }
    // 1. 포인트 사용


    // 1.2 사용을 실패하는 경우 - 사용할 포인트보다 보유한 포인트가 적은 경우
    // 1.6 사용할 포인트가 남은 포인트보다 적은 경우
    // 1.7 사용할 포인트가 남은 포인트와 같은 경우

    // 2. 포인트 충전
    @Test
    void chargeZeroPoint() {
        // 2.1 충전할 포인트가 0인 경우
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> pointService.chargePoint(1, 0));
    }

    @Test
    void chargeNegativePoint() {
        // 2.2 충전할 포인트가 0보다 작은 경우
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> pointService.chargePoint(1, -1));
    }

    @Test
    void chargeOverMaxPoint() {
        // 2.3 충전할 포인트가 최대치보다 큰 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 0, System.currentTimeMillis()));
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> pointService.chargePoint(1, 10_000_001));
    }

    @Test
    void chargeOverMaxPointSum() {
        // 2.4 충전할 포인트와 잔고의 합이 최대치보다 큰 경우.
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 5_000_000, System.currentTimeMillis()));
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> pointService.chargePoint(1, 5_000_001));
    }

    @Test
    void chargeMaxPoint() {
        // 2.5 충전할 포인트가 최대치와 같은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 5_000_000, System.currentTimeMillis()));
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하면 실패
        assertDoesNotThrow(() -> {
            pointService.chargePoint(1, 5_000_000);
        });
    }

    @Test
    void chargeLessThanMaxPoint() {
        // 2.6 충전할 포인트가 최대치보다 작은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 5_000_000, System.currentTimeMillis()));
        PointService pointService = new PointService(userPointRepositoryMock);

        // 예외가 발생하면 실패
        assertDoesNotThrow(() -> {
            pointService.chargePoint(1, 4_999_999);
        });
    }


    // 3. 포인트 조회
    // 3.1 조회할 사용자의 포인트를 한번도 충전 안한 경우
    // 3.2 조회할 사용자의 포인트를 한번 이상 충전한 경우
    // 3.3 조회할 사용자의 포인트를 한번 이상 충전하고 사용한 경우.


}
