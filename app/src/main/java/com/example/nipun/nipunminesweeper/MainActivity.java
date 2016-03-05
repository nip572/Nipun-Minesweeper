package com.example.nipun.nipunminesweeper;

/**
 * Created by Nipun on 3/2/16.
 */
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected boolean timeStart;
    protected boolean mines_set;
    protected boolean gameEnd;
    TextView count_mine;
    TextView timerVar;
    ImageButton button1;
    TableLayout mineFarm;
    private com.example.nipun.nipunminesweeper.GameBoard gameBoards[][];
    final private int squareSize =100;
    final private int rows = 12;
    final private int columns = 8;
    final private int totalMines = 30;
     Handler timer = new Handler();
     int timeElapsed = 0;
     private int mine;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        count_mine = (TextView) findViewById(R.id.MineCount);
        timerVar = (TextView) findViewById(R.id.Timer);

        button1 = (ImageButton) findViewById(R.id.Smiley);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGameplay();
                startGame();
            }
        });

        mineFarm = (TableLayout)findViewById(R.id.MineField);
        Toast.makeText(getApplicationContext(),
                "Click Smiley to play game. Good Luck !!!", Toast.LENGTH_LONG).show();


    }

    private void startGame()
    {

        createFarm();
        displayMineField();

        mine = totalMines;
        gameEnd = false;
        timeElapsed = 0;
    }

    private void displayMineField()
    {

        for (int row = 1; row < rows + 1; row++)
        {
            TableRow tableRow = new TableRow(this);


            tableRow.setLayoutParams(mineFarm.getLayoutParams());

            for (int column = 1; column < columns + 1; column++)
            {
                gameBoards[row][column].setLayoutParams(new TableRow.LayoutParams(
                        squareSize,
                        squareSize));
                tableRow.addView(gameBoards[row][column]);
            }
            mineFarm.addView(tableRow);

        }
    }



    public void timerStart()
    {
        if (timeElapsed == 0)
        {
            timer.removeCallbacks(updateTimeElasped);
            timer.postDelayed(updateTimeElasped, 1000);
        }
    }

    public void timerStop()
    {

        timer.removeCallbacks(updateTimeElasped);
    }

    //TIME THREAD
    private Runnable updateTimeElasped = new Runnable()
    {
        public void run()
        {
            long currentMilliseconds = System.currentTimeMillis();
            ++timeElapsed;

            if (timeElapsed < 10)
            {
                timerVar.setText("00" + Integer.toString(timeElapsed));
            }
            else if (timeElapsed < 100)
            {
                timerVar.setText("0" + Integer.toString(timeElapsed));
            }
            else
            {
                timerVar.setText(Integer.toString(timeElapsed));
            }

            timer.postAtTime(this,currentMilliseconds);
            timer.postDelayed(updateTimeElasped,1000);

        }
    };

    private void endGameplay()
    {
        timerStop();
        timerVar.setText("000");
        count_mine.setText("030");
        button1.setBackgroundResource(R.drawable.smilenipun);


        mineFarm.removeAllViews();


        timeStart = false;
        mines_set = false;
        gameEnd = false;
        mine = 0;
    }

    private void createFarm()
    {


        gameBoards = new GameBoard[rows + 2][columns + 2];

        for (int row = 0; row < rows + 2; row++)
        {
            for (int column = 0; column < columns + 2; column++)
            {
                gameBoards[row][column] = new GameBoard(this);
                gameBoards[row][column].setVar();


                final int currentRow = row;
                final int currentColumn = column;


                gameBoards[row][column].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        if (!timeStart)
                        {
                            timerStart();
                            timeStart = true;
                        }


                        if (!mines_set)
                        {
                            mines_set = true;
                            setBlock(currentRow, currentColumn);
                        }


                        if (!gameBoards[currentRow][currentColumn].isFlag())
                        {

                            unhideSeries(currentRow, currentColumn);


                            if (gameBoards[currentRow][currentColumn].containsMine())
                            {

                                endGameFinish(currentRow, currentColumn);
                            }


                            if (checkWin())
                            {

                                gameWon();
                            }
                        }
                    }
                });


                gameBoards[row][column].setOnLongClickListener(new View.OnLongClickListener()
                {
                    public boolean onLongClick(View view)
                    {

                        if (!gameBoards[currentRow][currentColumn].hidden() && (gameBoards[currentRow][currentColumn].getNumberOfMinesNeighbors() > 0) && !gameEnd)
                        {
                            int nearbyFlaggedBlocks = 0;
                            for (int previousRow = -1; previousRow < 2; previousRow++)
                            {
                                for (int previousColumn = -1; previousColumn < 2; previousColumn++)
                                {
                                    if (gameBoards[currentRow + previousRow][currentColumn + previousColumn].isFlag())
                                    {
                                        nearbyFlaggedBlocks++;
                                    }
                                }
                            }


                            if (nearbyFlaggedBlocks == gameBoards[currentRow][currentColumn].getNumberOfMinesNeighbors())
                            {
                                for (int previousRow = -1; previousRow < 2; previousRow++)
                                {
                                    for (int previousColumn = -1; previousColumn < 2; previousColumn++)
                                    {

                                        if (!gameBoards[currentRow + previousRow][currentColumn + previousColumn].isFlag())
                                        {

                                            unhideSeries(currentRow + previousRow, currentColumn + previousColumn);


                                            if (gameBoards[currentRow + previousRow][currentColumn + previousColumn].containsMine())
                                            {

                                                endGameFinish(currentRow + previousRow, currentColumn + previousColumn);
                                            }


                                            if (checkWin())
                                            {

                                                gameWon();
                                            }
                                        }
                                    }
                                }
                            }


                            return true;
                        }


                        if (gameBoards[currentRow][currentColumn].isClickable() &&
                                (gameBoards[currentRow][currentColumn].isEnabled() || gameBoards[currentRow][currentColumn].isFlag()))
                        {


                            if (!gameBoards[currentRow][currentColumn].isFlag() && !gameBoards[currentRow][currentColumn].isQuesMark())
                            {
                                gameBoards[currentRow][currentColumn].setBlockDisabledOrOpened(false);
                                gameBoards[currentRow][currentColumn].FlagIconSet(true);
                                gameBoards[currentRow][currentColumn].setFlag(true);
                                mine--; //reduce mine count
                                mineCountUpdate();
                            }

                            else if (!gameBoards[currentRow][currentColumn].isQuesMark())
                            {
                                gameBoards[currentRow][currentColumn].setBlockDisabledOrOpened(true);
                                gameBoards[currentRow][currentColumn].QuestionMark(true);
                                gameBoards[currentRow][currentColumn].setFlag(false);
                                gameBoards[currentRow][currentColumn].setQuesMark(true);
                                mine++;
                                mineCountUpdate();
                            }

                            else
                            {
                                gameBoards[currentRow][currentColumn].setBlockDisabledOrOpened(true);
                                gameBoards[currentRow][currentColumn].clearIcons();
                                gameBoards[currentRow][currentColumn].setQuesMark(false);

                                if (gameBoards[currentRow][currentColumn].isFlag())
                                {
                                    mine++;
                                    mineCountUpdate();
                                }

                                gameBoards[currentRow][currentColumn].setFlag(false);
                            }

                            mineCountUpdate();
                        }

                        return true;
                    }
                });
            }
        }
    }

    private boolean checkWin()
    {
        for (int row = 1; row < rows + 1; row++)
        {
            for (int column = 1; column < columns + 1; column++)
            {
                if (!gameBoards[row][column].containsMine() && gameBoards[row][column].hidden())
                {
                    return false;
                }
            }
        }
        return true;
    }

    private void mineCountUpdate()
    {
        if (mine < 0)
        {
            count_mine.setText(Integer.toString(mine));
        }
        else if (mine < 10)
        {
            count_mine.setText("00" + Integer.toString(mine));
        }
        else if (mine < 100)
        {
            count_mine.setText("0" + Integer.toString(mine));
        }
        else
        {
            count_mine.setText(Integer.toString(mine));
        }
    }

    private void gameWon()
    {
        timerStop();
        timeStart = false;
        gameEnd = true;
        mine = 0;


        button1.setBackgroundResource(R.drawable.goggles);

        mineCountUpdate();


        for (int row = 1; row < rows + 1; row++)
        {
            for (int column = 1; column < columns + 1; column++)
            {
                gameBoards[row][column].setClickable(false);
                if (gameBoards[row][column].containsMine())
                {
                    gameBoards[row][column].setBlockDisabledOrOpened(false);
                    gameBoards[row][column].FlagIconSet(true);
                }
            }
        }


        Toast.makeText(getApplicationContext(),
                "Wohooo you won !!!", Toast.LENGTH_LONG).show();

    }

    private void endGameFinish(int currentRow, int currentColumn)
    {
        gameEnd = true;
        timerStop();
        timeStart = false;
        button1.setBackgroundResource(R.drawable.bomb);


        for (int row = 1; row < rows + 1; row++)
        {
            for (int column = 1; column < columns + 1; column++)
            {

                gameBoards[row][column].setBlockDisabledOrOpened(false);


                if (gameBoards[row][column].containsMine() && !gameBoards[row][column].isFlag())
                {

                    gameBoards[row][column].setMineIcon(false);
                }

                if (!gameBoards[row][column].containsMine() && gameBoards[row][column].isFlag())
                {
                    gameBoards[row][column].FlagIconSet(false);
                }

                if (gameBoards[row][column].isFlag())
                {
                    gameBoards[row][column].setClickable(false);
                }
                gameBoards[row][column].setEnabled(false);
            }
        }

        gameBoards[currentRow][currentColumn].mineOpened();

        Toast.makeText(getApplicationContext(),
                " Hard luck you lost :( \n Click emoticon to play again. ", Toast.LENGTH_LONG).show();


    }

    private void setBlock(int row1, int column1)
    {

        Random rand = new Random();
        int mineRow, mineColumn;

        for (int row = 0; row < totalMines; row++)
        {
            mineRow = rand.nextInt(columns);
            mineColumn = rand.nextInt(rows);
            if ((mineRow + 1 != column1) || (mineColumn + 1 != row1))
            {
                if (gameBoards[mineColumn + 1][mineRow + 1].containsMine())
                {
                    row--;
                }

                gameBoards[mineColumn + 1][mineRow + 1].plantMine();
            }

            else
            {
                row--;
            }
        }

        int neighborMineCount;


        for (int row = 0; row < rows + 2; row++)
        {
            for (int column = 0; column < columns + 2; column++)
            {

                neighborMineCount = 0;
                if ((row != 0) && (row != (rows + 1)) && (column != 0) && (column != (columns + 1)))
                {

                    for (int previousRow = -1; previousRow < 2; previousRow++)
                    {
                        for (int previousColumn = -1; previousColumn < 2; previousColumn++)
                        {
                            if (gameBoards[row + previousRow][column + previousColumn].containsMine())
                            {

                                neighborMineCount++;
                            }
                        }
                    }

                    gameBoards[row][column].setNeighborsCount(neighborMineCount);
                }

                else
                {
                    gameBoards[row][column].setNeighborsCount(9);
                    gameBoards[row][column].UncoverBlock();
                }
            }
        }
    }

    private void unhideSeries(int clickedRow, int clickedColumn)
    {

        if (gameBoards[clickedRow][clickedColumn].containsMine() || gameBoards[clickedRow][clickedColumn].isFlag())
        {
            return;
        }


        gameBoards[clickedRow][clickedColumn].UncoverBlock();


        if (gameBoards[clickedRow][clickedColumn].getNumberOfMinesNeighbors() != 0 )
        {
            return;
        }


        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {

                if (gameBoards[clickedRow + row - 1][clickedColumn + column - 1].hidden()
                        && (clickedRow + row - 1 > 0) && (clickedColumn + column - 1 > 0)
                        && (clickedRow + row - 1 < rows + 1) && (clickedColumn + column - 1 < columns + 1))
                {
                    unhideSeries(clickedRow + row - 1, clickedColumn + column - 1);
                }
            }
        }
        return;
    }



}
