package com.anuj;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class PlayGame {
    SnakeAndLadderBoard snakeAndLadderBoard;
    Map<Integer, Integer> snakeMap;
    Map<Integer, Integer> ladderMap;
    PlayGame(){
        snakeAndLadderBoard = new SnakeAndLadderBoard();
        snakeMap = snakeAndLadderBoard.snakeMap;
        ladderMap = snakeAndLadderBoard.ladderMap;

    }


    void playGame(Map<String, Integer> players, Scanner scanner) {
        boolean isEnd = false;

        while (!isEnd) {
            for (Map.Entry<String, Integer> player : players.entrySet()) {
                int diceValue = getRandomNumber();
                int currentPosition = player.getValue();
                int movedPosition = player.getValue();
                if(currentPosition + diceValue <= 100){
                    movedPosition = currentPosition + diceValue;
                }
                 if (movedPosition < 100) {
                     movedPosition = currentPosition + diceValue;
                    if (ladderMap.containsKey(movedPosition)) {
                        movedPosition = ladderMap.get(movedPosition);
                    } else if (snakeMap.containsKey(movedPosition)) {
                        movedPosition = snakeMap.get(movedPosition);
                    }
                    player.setValue(movedPosition);
                }
                System.out.println(player.getKey() + " rolled a " + diceValue + " and moved from " + currentPosition + " to " + movedPosition);
                 if (movedPosition == 100) {
                         isEnd = true;
                         System.out.println(player.getKey()+" wins the game\n");
                         break;
                     }

            }

        }

    }
    int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
