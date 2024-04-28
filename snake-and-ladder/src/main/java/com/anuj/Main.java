package com.anuj;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        PlayGame playGame = new PlayGame();
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
//        int no_of_snake = scanner.nextInt();
//        for(int i = 0; i < no_of_snake; i++){
//            int head = scanner.nextInt();
//            int tail = scanner.nextInt();
//            playGame.snakeMap.put(head, tail);
//        }
//        int no_of_ladder = scanner.nextInt();
//        for(int i = 0; i < no_of_ladder; i++){
//            int start = scanner.nextInt();
//            int end = scanner.nextInt();
//            playGame.snakeMap.put(start, end);
//        }
        int no_of_player = scanner.nextInt();
        Map<String, Integer> players = new HashMap<>();
        while (no_of_player > 0){
            players.put(scanner.next(), 0);
            no_of_player--;
        }
        String s ="sss";
        if(s.charAt(0)==' '){
            System.out.printf("dddd");
        }
        playGame.playGame(players, scanner);
    }
}