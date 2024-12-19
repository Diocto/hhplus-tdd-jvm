package io.hhplus.tdd.point;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConcurrencyTest {
    private static final long INIT_POINT = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testConcurrentUseAndCharge() throws Exception {
        int threadCount = 50;
        long userId = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        mockMvc.perform(patch(String.format("/point/%d/charge", userId))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(INIT_POINT)))
                .andExpect(status().isOk());

        for (int i = 0; i < threadCount; i++) {
            int threadId = i;
            executorService.submit(() -> {
                try {
                    if (threadId % 2 == 0) { // 짝수 쓰레드는 포인트 사용
                        mockMvc.perform(patch(String.format("/point/%d/use", userId))
                                        .contentType("application/json")
                                        .content(objectMapper.writeValueAsString(50)))
                                .andExpect(status().isOk());
                    } else { // 홀수 쓰레드는 포인트 충전
                        mockMvc.perform(patch(String.format("/point/%d/charge", userId))
                                        .contentType("application/json")
                                        .content(objectMapper.writeValueAsString(100)))
                                .andExpect(status().isOk());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 쓰레드가 완료될 때까지 대기
        executorService.shutdown();


        // 최종 포인트 잔액 조회 및 검증
        String response = mockMvc.perform(get(String.format("/point/%d", userId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode responseBody = objectMapper.readTree(response);

        int finalBalance = responseBody.get("point").asInt();

        // 예상 잔액 계산
        int chargeCount = threadCount / 2; // 충전 요청 수
        int useCount = threadCount / 2; // 사용 요청 수
        long expectedBalance = INIT_POINT + (chargeCount * 100) - (useCount * 50);

        // 실제 값과 예상 값을 비교
        assertEquals(expectedBalance, finalBalance, "최종 잔액이 예상 값과 다릅니다.");
    }
}