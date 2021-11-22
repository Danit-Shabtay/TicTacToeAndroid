package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TicTacToeBoard extends View {

    private final int boardColor;
    private final int XColor;
    private final int OColor;
    private final int winningLineColor;
    private final Paint paint = new Paint();
    private final GameLogic game;
    private int cellSize = getWidth()/3;

    public TicTacToeBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        game = new GameLogic();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicTacToeBoard,0,0);

        try {
            boardColor = a.getInteger(R.styleable.TicTacToeBoard_boardColor, 0);
            XColor = a.getInteger(R.styleable.TicTacToeBoard_XColor, 0);
            OColor = a.getInteger(R.styleable.TicTacToeBoard_OColor, 0);
            winningLineColor = a.getInteger(R.styleable.TicTacToeBoard_winningLineColor, 0);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension/3;

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);
        drawMarkers(canvas);

        //drawX(canvas, 2, 2);
        //drawO(canvas, 1, 1);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN){
            int row = (int) Math.ceil(y/cellSize);
            int col = (int) Math.ceil(x/cellSize);

            if (game.updateGameBoard(row, col)){
                invalidate();

                // Updating the players turn
                if (game.getPlayer() % 2 == 0){
                    game.setPlayer(game.getPlayer()-1);
                }
                else {
                    game.setPlayer(game.getPlayer()+1);
                }
            }

            invalidate();

            return true;
        }

        return false;
    }

    private void drawGameBoard(Canvas canvas){
        paint.setColor(boardColor);
        paint.setStrokeWidth(16);

        for (int c=1; c<3; c++){
            canvas.drawLine(cellSize*c, 0, cellSize*c, canvas.getWidth(), paint);
        }

        for (int r=1; r<3; r++){
            canvas.drawLine(0,cellSize*r, canvas.getWidth(),cellSize*r, paint);
        }
    }

    private void drawMarkers(Canvas canvas){
        for (int r=0; r<3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.getGameBoard()[r][c] != 0) {
                    if (game.getGameBoard()[r][c] == 1){
                        drawX(canvas, r, c);
                    }
                    else {
                        drawO(canvas, r, c);
                    }
                }
            }
        }
    }

    private void drawX(Canvas canvas, int row, int col){
        paint.setColor(XColor);

        canvas.drawLine((float) ((col+1)*cellSize - cellSize*0.2),
                        (float) (row*cellSize + cellSize*0.2),
                        (float) (col*cellSize + cellSize*0.2),
                        (float) ((row+1)*cellSize - cellSize*0.2),
                        paint);

        canvas.drawLine((float) (col*cellSize + cellSize*0.2),
                        (float) (row*cellSize + cellSize*0.2),
                        (float) ((col+1)*cellSize - cellSize*0.2),
                        (float) ((row+1)*cellSize - cellSize*0.2),
                        paint);
    }

    private void drawO(Canvas canvas, int row, int col){
        paint.setColor(OColor);

        canvas.drawOval((float) (col*cellSize + cellSize*0.2),
                        (float) (row*cellSize + cellSize*0.2),
                        (float) ((col*cellSize + cellSize) - cellSize*0.2),
                        (float) ((row+cellSize + cellSize) - cellSize*0.2),
                        paint);

    }
}
