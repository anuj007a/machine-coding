package com.tictactoe.validator;

import com.tictactoe.model.Board;

public class MoveValidator {

    Board board;

    public MoveValidator(Board board){
        this.board=board;
    }

    public boolean validateIsBoardPositionEmpty(int x, int y){
        return board.getPosition(x,y).equals("-");
    }

    public boolean isValidMove(int x, int y){
        return x < board.getBoard().length+1 && y < board.getBoard().length+1 && x >= 0 && y >= 0;
    }

}
