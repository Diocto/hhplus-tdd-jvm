package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public class PointControllerTest {
    @InjectMocks
    private PointController pointController;

    @Mock
    private PointService pointService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mock 초기화
        pointController = new PointController(pointService);
    }

    // 목적으로 하는 함수를 호출하였는지
    // 서비스 레이어에서 리턴한 값을 반환하는지

    @Test
    void viewPoint(){
        //
    }

    @Test
    void chargePoint(){
        //
    }

    @Test
    void usePoint(){
        //
    }
}
