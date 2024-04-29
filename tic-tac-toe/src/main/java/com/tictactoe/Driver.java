package com.tictactoe;

import com.tictactoe.model.Player;
import com.tictactoe.service.TicTocToeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String player1 = scanner.next();
        String player2 = scanner.next();
        ArrayList<Player> players =new ArrayList<>();
        players.add(new Player(player1,"X"));
        players.add(new Player(player2,"O"));
        TicTocToeService toeService = new TicTocToeService(3, players);
        toeService.playGame();

    }
}
