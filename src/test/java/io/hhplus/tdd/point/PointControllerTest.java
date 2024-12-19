package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PointControllerTest {
    @InjectMocks
    private PointController uut;

    @Mock
    private PointService pointServiceMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
        uut = new PointController(pointServiceMock);
    }

    // 목적으로 하는 함수를 호출하였는지
    // 서비스 레이어에서 리턴한 값을 반환하는지
    @Test
    void viewPoint(){
        UserPoint givenUserPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        when(pointServiceMock.viewPoint(1L)).thenReturn(givenUserPoint);
        UserPoint userpoint = uut.point(1L);
        verify(pointServiceMock, times(1)).viewPoint(1L);
        assertEquals(userpoint, givenUserPoint);
    }

    @Test
    void chargePoint(){
        UserPoint givenUserPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        when(pointServiceMock.chargePoint(1L, 10L)).thenReturn(givenUserPoint);
        UserPoint userpoint = uut.charge(1L, 10L);
        verify(pointServiceMock, times(1)).chargePoint(1L, 10L);
        assertEquals(userpoint, givenUserPoint);
    }

    @Test
    void usePoint(){
        UserPoint givenUserPoint = new UserPoint(1L, 10L, System.currentTimeMillis());
        when(pointServiceMock.usePoint(1L, 10L)).thenReturn(givenUserPoint);
        UserPoint userpoint = uut.use(1L, 10L);
        verify(pointServiceMock, times(1)).usePoint(1L, 10L);
        assertEquals(userpoint, givenUserPoint);
    }
}
