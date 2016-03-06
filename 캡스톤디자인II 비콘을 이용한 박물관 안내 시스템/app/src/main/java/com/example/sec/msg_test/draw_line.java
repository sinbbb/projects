package com.example.sec.msg_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by sec on 2015-06-22.
 */
public class draw_line extends View {
    int[][] beacon_position = {{160, 50}, {110, 90}, {110, 170}, {110, 250}, {110, 330}, {110, 415}, {160, 455}};
    int[][] beacon_cposition = {{160, 50}, {110, 50}, {110, 90}, {110, 170}, {110, 250}, {110, 330}, {110, 415}, {110, 455}, {160, 455}};
    int yrh;
    int destination;
    boolean change = false;

    // public int beacon_id;
    public draw_line(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //yrh = Integer.valueOf(MainActivity.beacon_id);
        yrh = Integer.parseInt(MainActivity.beacon_id) - 1;
        //destination = Integer.valueOf(MainActivity.b_destination) - 1;
        destination = Integer.parseInt(MainActivity.b_destination) - 1;
        //yrh = MainActivity.beacon_id;
        //destination = MainActivity.b_destination;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);


        //yrh = 2;
        //destination = 5;
        Path path = new Path();
        path.moveTo(31, 0);
        path.lineTo(51, 59);
        path.lineTo(0, 23);
        path.lineTo(63, 23);
        path.lineTo(12, 59);
        path.close();
        float xdensity = getResources().getDisplayMetrics().xdpi / 160f;
        float ydensity = getResources().getDisplayMetrics().ydpi / 160f;
        if (destination == 0 || destination == 6)
            path.offset(beacon_position[destination][0] * xdensity - 13, beacon_position[destination][1] * ydensity - 28);
        else if (destination > 0 && destination < 6)
            path.offset(beacon_position[destination][0] * xdensity - 32, beacon_position[destination][1] * ydensity - 28);
        if (MainActivity.click_route && !MainActivity.click_yrh) {
            canvas.drawPath(path, paint);
        } else if (MainActivity.click_yrh || MainActivity.click_route) {
            if (yrh > destination) change = true;
            if (change) {
                int swap;
                swap = yrh;
                yrh = destination;
                destination = swap;
                if (MainActivity.click_route) {
                    canvas.drawPath(path, paint);
                } else if (MainActivity.click_yrh)
                    canvas.drawCircle(beacon_position[yrh][0] * xdensity, beacon_position[yrh][1] * ydensity, 10, paint);
            } else
                canvas.drawCircle(beacon_position[yrh][0] * xdensity, beacon_position[yrh][1] * ydensity, 10, paint);

            if (MainActivity.click_route) {
                if (yrh == 0 && destination < 6) {
                    int i = 0;
                    for (i = yrh; i < destination + 1; i++) {
                        canvas.drawLine(beacon_cposition[i][0] * xdensity, beacon_cposition[i][1] * ydensity, beacon_cposition[i + 1][0] * xdensity, beacon_cposition[i + 1][1] * ydensity, paint);
                    }
                    if (change)
                        canvas.drawCircle(beacon_position[i - 1][0] * xdensity, beacon_position[i - 1][1] * ydensity, 10, paint);
                    else canvas.drawPath(path, paint);
                } else if (yrh > 0 && destination < 6) {
                    int i = 0;
                    for (i = yrh + 1; i < destination + 1; i++) {
                        canvas.drawLine(beacon_cposition[i][0] * xdensity, beacon_cposition[i][1] * ydensity, beacon_cposition[i + 1][0] * xdensity, beacon_cposition[i + 1][1] * ydensity, paint);
                    }
                    if (change)
                        canvas.drawCircle(beacon_position[i - 1][0] * xdensity, beacon_position[i - 1][1] * ydensity, 10, paint);
                    else canvas.drawPath(path, paint);

                } else if (yrh > 0 && destination == 6) {
                    int i = 0;
                    for (i = yrh + 1; i <= destination + 1; i++) {
                        canvas.drawLine(beacon_cposition[i][0] * xdensity, beacon_cposition[i][1] * ydensity, beacon_cposition[i + 1][0] * xdensity, beacon_cposition[i + 1][1] * ydensity, paint);
                    }
                    if (change)
                        canvas.drawCircle(beacon_position[i - 2][0] * xdensity, beacon_position[i - 2][1] * ydensity, 10, paint);
                    else canvas.drawPath(path, paint);
                } else if (yrh == 0 && destination == 6) {
                    int i = 0;
                    for (i = yrh; i <= destination + 1; i++) {
                        canvas.drawLine(beacon_cposition[i][0] * xdensity, beacon_cposition[i][1] * ydensity, beacon_cposition[i + 1][0] * xdensity, beacon_cposition[i + 1][1] * ydensity, paint);
                    }
                    if (change)
                        canvas.drawCircle(beacon_position[i - 2][0] * xdensity, beacon_position[i - 2][1] * ydensity, 10, paint);
                    else canvas.drawPath(path, paint);
                }
                //canvas.drawLine(110*xdensity, 50*ydensity, 160*xdensity, 50*ydensity, paint);
                super.onDraw(canvas);
            }
            if (change) {
                int swap;
                swap = yrh;
                yrh = destination;
                destination = swap;
            }
        }
    }
}
