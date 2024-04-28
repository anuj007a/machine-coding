package com.anuj;

import java.util.HashMap;
import java.util.Map;

public class SnakeAndLadderBoard {
    Map<Integer, Integer> snakeMap;
    Map<Integer, Integer> ladderMap;
    SnakeAndLadderBoard(){
        snakeMap = new HashMap<>(){{
            put(62, 5);
            put(33, 6);
            put(49, 9);
            put(88, 16);
            put(41, 20);
            put(56, 53);
            put(98, 64);
            put(93, 73);
            put(95, 75);    }};
        ladderMap = new HashMap<>(){{
            put(2,37);
            put(27, 46);
            put(10, 32);
            put(51, 68);
            put(61, 79);
            put(65, 84);
            put(71, 91);
            put(81, 100);
        }};
    }
//    Map<Integer, Integer> snakeMap = new HashMap<>(){{
//        put(62, 5);
//        put(33, 6);
//        put(49, 9);
//        put(88, 16);
//        put(41, 20);
//        put(56, 53);
//        put(98, 64);
//        put(93, 73);
//        put(95, 75);    }};
//    Map<Integer, Integer> ladderMap = new HashMap<>(){{
//        put(2,37);
//        put(27, 46);
//        put(10, 32);
//        put(51, 68);
//        put(61, 79);
//        put(65, 84);
//        put(71, 91);
//        put(81, 100);
//    }};
}
