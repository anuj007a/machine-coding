package com.tictactoe.model;

import java.util.Arrays;
import java.util.Objects;

public class Board {
    private String[][] board;

    public Board(int N) {
        board = new String[N][N];
        for (String[] strings : board) {
            for (int j = 0; j < board.length; j++) {
                strings[j] = "-";
            }
        }
        int cddd=0;
    }

    public void printBoard() {
        for (String[] strings : board) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }
    }

    public String getPosition(int x, int y){
        return board[x][y];
    }

    public String[][] getBoard(){
        return this.board;
    }

    public void setPosition(int x, int y, String symbol) {
        board[x][y] = symbol;
    }

    public boolean boardExhausted(){
        return Arrays.stream(board).anyMatch(row -> Arrays.asList(row).contains("-"));
    }
}
