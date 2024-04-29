package com.tictactoe.service;

import com.tictactoe.model.Board;
import com.tictactoe.model.Player;
import com.tictactoe.validator.MoveValidator;

import java.util.List;
import java.util.Scanner;

public class TicTocToeService {

    Board board;
    List<Player> players;
    Scanner scan = new Scanner(System.in);
    MoveValidator moveValidator;

    public TicTocToeService(int N, List<Player> players) {
        this.board = new Board(N);
        this.players = players;
        this.moveValidator = new MoveValidator(board);
    }

    public void playGame() {
        board.printBoard();
        int playerResetCounter = 0;
        while (board.boardExhausted()) {
            String xValue = scan.next().toLowerCase();
            if(xValue.equals("exit"))
                System.exit(0);
            String yValue = scan.next().toLowerCase();
            if(yValue.equals("exit"))
                System.exit(0);

            int x = Integer.parseInt(xValue)-1;
            int y = Integer.parseInt(yValue)-1;

            if (playerResetCounter == players.size()) {
                playerResetCounter = 0;
            }
            if (moveValidator.isValidMove(x, y) && moveValidator.validateIsBoardPositionEmpty(x, y)) {
                Player player = players.get(playerResetCounter);
                board.setPosition(x, y, player.getSymbol());
                board.printBoard();
                if (checkPlayerWin(x, y, player.getSymbol())) {
                    System.out.println(player.getName() + " won the game");
                }
                playerResetCounter++;
            } else {
                System.out.println("Invalid input");
            }
        }
        System.out.println("Game Over");
    }

    private boolean checkPlayerWin(int row, int col, String symbol) {
        boolean winRow = true, winCol = true, winLeft = true, winRight = true;
        for (int i = 0; i < board.getBoard().length; i++) {
            if (!board.getPosition(row, i).equals(symbol)) {
                winRow = false;
            }
            if (!board.getPosition(i, col).equals(symbol)) {
                winCol = false;
            }
            if (!board.getPosition(i, i).equals(symbol)) {
                winLeft = false;
            }
            if (!board.getPosition(i, board.getBoard().length - i - 1).equals(symbol)) {
                winRight = false;
            }
        }
        return winRow || winCol || winLeft || winRight;
    }
}
