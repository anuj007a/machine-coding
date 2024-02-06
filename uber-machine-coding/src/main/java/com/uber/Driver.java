package com.uber;

import com.uber.admin.AdminUtil;
import com.uber.consumer.Consumer;
import com.uber.consumer.ConsumerImpl;
import com.uber.producer.Producer;
import com.uber.producer.ProducerImpl;

public class Driver {

    public static void main(String[] args) throws InterruptedException {
        AdminUtil admin  = new AdminUtil();
        admin.addChannel("uber1");
        admin.addChannel("uber2");

        Producer uber1 = new ProducerImpl("uber1");
        Producer uber2 = new ProducerImpl("uber2");

        Thread thread = new Thread(() -> {
            while (true) {
                uber1.publish("123");
                uber1.publish("445");
                uber1.publish("555");
                try { Thread.sleep(100); } catch (InterruptedException ignored) { }
            }
        });
        thread.start();
        Thread thread2 = new Thread(() -> {
            while (true) {
                uber2.publish("000");
                uber2.publish("999");
                uber2.publish("65");
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            }
        });
        thread2.start();

        Consumer consumer1 = new ConsumerImpl("c1", "uber1", 2);
        Consumer consumer2 = new ConsumerImpl("c2", "uber2", 5);
        Thread thread3 = new Thread(() -> {
            while (true) {
                System.out.println("C1 : " + consumer1.fetchMessages());
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            }
        });
        thread3.start();
        Thread thread4 = new Thread(() -> {
            while (true) {
                System.out.println("C2 : " + consumer2.fetchMessages());
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            }
        });
        thread4.start();
        Thread.sleep(5000);
        consumer2.updateOffset(0);
    }

}
