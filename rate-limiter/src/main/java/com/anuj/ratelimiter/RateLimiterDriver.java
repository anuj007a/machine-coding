package com.wraith.ratelimiter;

import com.wraith.ratelimiter.model.ThrottleRule;
import com.wraith.ratelimiter.service.ThrotleRulesService;
import com.wraith.ratelimiter.service.UserIdentificationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RateLimiterDriver {
    public static void main(String[] args) throws Exception {

        ThrottleRule rule = new ThrottleRule();
        ThrotleRulesService throtleRulesService = ThrotleRulesService.getInstance();
        throtleRulesService.createRule("client1", rule);

        UserIdentificationService request = new UserIdentificationService();

        ExecutorService executor = Executors.newFixedThreadPool(1);

        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);

        Long startTime = System.currentTimeMillis();

//        for (int i = 0; i < 12; i++) {
//            executor.execute(() -> {
//                System.out.println(" client1 "+Thread.currentThread().getName() + "--" + request.serveRequest("client1"));
//          //      System.out.println(" client2 "+Thread.currentThread().getName() + "--" + handleRequest.serveRequest("client2"));
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
        Runnable r = () -> {
            System.out.println(" client1 " + Thread.currentThread().getName() + "--" + request.serveRequest("client1"));
        };
        scheduledExecutor.scheduleAtFixedRate(r, 0, 50, TimeUnit.MILLISECONDS);
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            Long endTime = System.currentTimeMillis();
            System.out.println("total time " + (endTime - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}