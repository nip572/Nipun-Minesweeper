package com.example.nipun.nipunminesweeper;

/**
 * Created by Nipun on 2/28/16.
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;


public class GameBoard extends Button {

    int neighborsMine;
    boolean clickable;
    boolean mine;
    boolean Flag;
    boolean covered;
    boolean quesMark;

    public GameBoard(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GameBoard(Context context)
    {
        super(context);
    }

    public GameBoard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setMineIcon(boolean enabled)
    {
        this.setText("MINE");

        if (!enabled)
        {
            //this.setBackgroundResource(R.drawable.square_brown);
            this.setBackgroundResource(R.drawable.grass);
            this.setTextColor(Color.RED);
        }
        else
        {
            int blackColorValue = Color.parseColor("#000000");
            ///cHANGE HERE MINE WALA
            //this.setBackgroundResource(R.drawable.goggles);
            this.setBackgroundColor(blackColorValue);

        }
    }

    public void setVar()
    {
        covered = clickable = true;
        mine = Flag = quesMark = false;
        neighborsMine = 0;
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size));
        this.setBackgroundResource(R.drawable.grass);
        boldFont();
    }


    public void updateNearbyMinesImage(int number)
    {
        this.setBackgroundResource(R.drawable.mud);

        getMineCount(number);
    }

    public void UncoverBlock()
    {
        if (!covered)
            return;

        setBlockDisabledOrOpened(false);
        covered = false;


        if (containsMine())
        {
            setMineIcon(false);
        }

        else
        {
            updateNearbyMinesImage(neighborsMine);
        }
    }

    public void setBlockDisabledOrOpened(boolean enabled)
    {
        if (!enabled)
        {
            this.setBackgroundResource(R.drawable.mud);
        }
        else
        {
            this.setBackgroundResource(R.drawable.grass);
        }
    }

    public void FlagIconSet(boolean enabled)
    {
        this.setText("FLAG");

        if (!enabled)
        {
            this.setBackgroundResource(R.drawable.mud);
            this.setTextColor(Color.MAGENTA);
        }
        else
        {
            this.setBackgroundResource(R.drawable.grass);
            this.setTextColor(Color.MAGENTA);
        }
    }

    private void boldFont()
    {
        this.setTypeface(null, Typeface.BOLD);
    }

    public void QuestionMark(boolean enabled)
    {
        this.setText("?");

        if (!enabled)
        {
            this.setBackgroundResource(R.drawable.grass);
            this.setTextColor(Color.RED);
        }
        else
        {
            this.setTextColor(Color.BLACK);
        }
    }

    public void clearIcons()
    {
        this.setText("");
    }

    public void getMineCount(int text)
    {
        if (text != 0)
        {
            this.setText(Integer.toString(text));


            switch (text)
            {
                case 1:
                    this.setTextColor(Color.BLUE);
                    break;
                case 2:
                    this.setTextColor(Color.rgb(0, 100, 0));
                    break;
                case 3:
                    this.setTextColor(Color.RED);
                    break;
                case 4:
                    this.setTextColor(Color.rgb(85, 26, 139));
                    break;
                case 5:
                    this.setTextColor(Color.rgb(139, 28, 98));
                    break;
                case 6:
                    this.setTextColor(Color.rgb(238, 173, 14));
                    break;
                case 7:
                    this.setTextColor(Color.rgb(47, 79, 79));
                    break;
                case 8:
                    this.setTextColor(Color.rgb(71, 71, 71));
                    break;
                case 9:
                    this.setTextColor(Color.rgb(205, 205, 0));
                    break;
            }
        }
    }

    public void mineOpened()
    {
        setMineIcon(true);
        this.setTextColor(Color.RED);
    }

    public void plantMine()
    {
        mine = true;
    }

    public boolean hidden()
    {
        return covered;
    }

    public boolean containsMine()
    {
        return mine;
    }

    public void setNeighborsCount(int number)
    {
        neighborsMine = number;
    }

    public int getNumberOfMinesNeighbors()
    {
        return neighborsMine;
    }

    public boolean isFlag()
    {
        return Flag;
    }

    public void setFlag(boolean flag)
    {
        Flag = flag;
    }

    public boolean isQuesMark()
    {
        return quesMark;
    }

    public void setQuesMark(boolean quesMark)
    {
        this.quesMark = quesMark;
    }

    public boolean isClickable()
    {
        return clickable;
    }

    public void setClickable(boolean clickable)
    {
        this.clickable = clickable;
    }

}
