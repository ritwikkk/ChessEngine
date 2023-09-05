package com.chess.engine.player;

public enum MoveStatus {
    Done {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    IllegalMove {
        @Override
        public boolean isDone() {
            return true;
        }
    }, LeavesPlayerInCheck {
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
