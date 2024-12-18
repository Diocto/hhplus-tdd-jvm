package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.UserPointBadUsageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PointServiceTest {
    // 서비스 테스트케이스
    // 1. 실패하는 케이스부터 구현할 것
    // 2. 예외 케이스부터 구현할 것
    // 3. 성공하는 케이스를 구현할 것

    @Mock
    private UserPointRepository userPointRepositoryMock;

    private PointService uut;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
        uut = new PointService(userPointRepositoryMock);
    }

    @Test
    void useZeroPoint() {
        // 1.1 사용을 실패하는 경우 - 사용할 포인트가 0
        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.usePoint(1L, 0L));
    }

    @Test
    void useNegativePoint() {
        // 1.2 사용할 포인트가 0보다 작은 경우

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.usePoint(1L, -1L));
    }

    @Test
    void useOverPoint() {
        // 1.3 사용을 실패하는 경우 - 사용할 포인트보다 보유한 포인트가 적은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 0, System.currentTimeMillis()));

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.usePoint(1L, 1L));
    }

    @Test
    void useLessThanPoint() {
        // 1.4 사용할 포인트가 남은 포인트보다 적은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 2, System.currentTimeMillis()));

        // 예외가 발생하면 실패
        assertDoesNotThrow(() -> uut.usePoint(1L, 1L));
        verify(userPointRepositoryMock, times(1)).save(1L, 1L);
    }
    @Test
    void useSamePoint() {
        // 1.5 사용할 포인트가 남은 포인트와 같은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 1, System.currentTimeMillis()));

        // 예외가 발생하면 실패
        assertDoesNotThrow(() -> uut.usePoint(1L, 1L));
        verify(userPointRepositoryMock, times(1)).save(1L, 0L);
    }

    // 2. 포인트 충전
    @Test
    void chargeZeroPoint() {
        // 2.1 충전할 포인트가 0인 경우

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.chargePoint(1L, 0L));
    }

    @Test
    void chargeNegativePoint() {
        // 2.2 충전할 포인트가 0보다 작은 경우

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.chargePoint(1L, -1L));
    }

    @Test
    void chargeOverMaxPoint() {
        // 2.3 충전할 포인트가 최대치보다 큰 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 0, System.currentTimeMillis()));

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.chargePoint(1L, 10_000_001L));
    }

    @Test
    void chargeOverMaxPointSum() {
        // 2.4 충전할 포인트와 잔고의 합이 최대치보다 큰 경우.
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 5_000_000, System.currentTimeMillis()));

        // 예외가 발생하지 않으면 실패
        assertThrows(UserPointBadUsageException.class, () -> uut.chargePoint(1L, 5_000_001L));
    }

    @Test
    void chargeMaxPoint() {
        // 2.5 충전할 포인트가 최대치와 같은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 5_000_000, System.currentTimeMillis()));

        // 예외가 발생하면 실패
        assertDoesNotThrow(() -> uut.chargePoint(1L, 5_000_000L));
        verify(userPointRepositoryMock, times(1)).save(1L, 10_000_000L);
    }

    @Test
    void chargeLessThanMaxPoint() {
        // 2.6 충전할 포인트가 최대치보다 작은 경우
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 5_000_000, System.currentTimeMillis()));

        // 예외가 발생하면 실패
        assertDoesNotThrow(() -> uut.chargePoint(1L, 4_999_999L));
        verify(userPointRepositoryMock, times(1)).save(1L, 9_999_999L);
    }

    // 3. 포인트 조회
    // 조회 함수의 책임은 그저 repository 를 호출하여 해당 값을 전달 해 주는 것으로, 테스트 케이스는 repository 가 반환하는 값을 확인하는 것으로 충분하다.
    @Test
    void viewPointNeverCharge() {
        // 3.1 유저의 포인트를 조회하는 경우.
        when(userPointRepositoryMock.findByUserId(1)).thenReturn(new UserPoint(1, 100, System.currentTimeMillis()));

        // 예외가 발생하면 실패
        // 포인트는 0원이어야 함
        assertDoesNotThrow(() -> {
            UserPoint userPoint = uut.viewPoint(1);
            assert userPoint.point() == 100;
        });
    }
}
