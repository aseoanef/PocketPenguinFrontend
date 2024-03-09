package com.example.pocketpenguin;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class PPButton extends Button {

    public PPButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Tema.colorTema == ColorTema.GREEN)    {
            setBackgroundTintList(getContext().getResources().getColorStateList(R.color.list_color_green));

        }
       // setBackgroundColor(getContext().getResources().getColor(R.color.green));
        if (Tema.colorTema == ColorTema.YELLOW)    {
            setBackgroundTintList(getContext().getResources().getColorStateList(R.color.list_color_yellow));

        }
        if (Tema.colorTema == ColorTema.RED)    {
            setBackgroundTintList(getContext().getResources().getColorStateList(R.color.list_color_red));

        }
        if (Tema.colorTema == ColorTema.BLUE)    {
            setBackgroundTintList(getContext().getResources().getColorStateList(R.color.list_color_blue));

        }
        if (Tema.colorTema == ColorTema.BLUELIGHT)    {
            setBackgroundTintList(getContext().getResources().getColorStateList(R.color.list_color_blue_light));

        }
        if (Tema.colorTema == ColorTema.BLUEDARK)    {
            setBackgroundTintList(getContext().getResources().getColorStateList(R.color.list_color_blue_dark));

        }
    }
}
