package com.example.tabbed_activity;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class FloatingViewService extends Service implements View.OnClickListener {


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    private Button button;
    private TextView textView;
    private Boolean ismoved = false;
    private Integer bg_color;

    SharedPreferences pref;

    public FloatingViewService() {
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int stratId){
//        bg_color = intent.getStringExtra("bgcolor");
//        Log.d("COLOR", "Getbgcolor" + bg_color);
//        Log.d("COLOR", bg_color);
//        Log.d("COLOR", String.valueOf(Integer.parseInt(bg_color)));
//        expandedView.setBackgroundColor(Integer.parseInt(bg_color));
//        return stratId;
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    int[] btnArr = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
    int[] txtviewArr = {R.id.buttontextview1, R.id.buttontextview2, R.id.buttontextview3, R.id.buttontextview4, R.id.buttontextview5, R.id.buttontextview6, R.id.buttontextview7, R.id.buttontextview8, R.id.buttontextview9};

    @Override
    public void onCreate() {
        super.onCreate();

        //getting the widget layout from xml using layout inflater
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        String appName;
        for (int i = 0; i < 9; i++) {
            button = mFloatingView.findViewById(btnArr[i]);
            button.setOnClickListener(listener);
            textView = mFloatingView.findViewById(txtviewArr[i]);
            appName = pref.getString((i + 1) + "_name", null);
            if (pref.getString(Integer.toString(i+1), null) == null) {
                textView.setVisibility(View.INVISIBLE);
            }
            else {
                textView.setText(appName);
            }
            //set quick menu button icon to app's icon
            try {
                Drawable icon = getPackageManager().getApplicationIcon(pref.getString(String.valueOf(i + 1), null));
                button.setBackground(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);

        //adding click listener to close button and expanded view
        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        bg_color = pref.getInt("bg_color", -1);
        if(bg_color != -1) {
            GradientDrawable bg = (GradientDrawable) expandedView.getBackground();
            bg.setColor(bg_color);
        }

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        if (!ismoved) {
                            collapsedView.setVisibility(View.GONE);
                            expandedView.setVisibility(View.VISIBLE);
                        } else {
                            ismoved = false;
                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        float dx = Math.abs(params.x - initialX);
                        float dy = Math.abs(params.y - initialY);
                        if (dx > 10 || dy > 10) {
                            mWindowManager.updateViewLayout(mFloatingView, params);
                            ismoved = true;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private int findBtnIdx(int btnid){
        for(int i=0; i<btnArr.length; i++){
            if(btnid == btnArr[i])
                return i;
        }
        return -1;
    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int btnIdx = findBtnIdx(view.getId());
            String id = pref.getString(String.valueOf(btnIdx+1), null); //해당값 불러오는 것, 해당값이 없을 경우 null호출
            if(id!=null) {
                Intent myintent = getPackageManager().getLaunchIntentForPackage(id);
                startActivity(myintent);
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(getApplicationContext(), "퀵메뉴를 지정해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.buttonClose:
                //closing the widget
                stopSelf();
                break;
        }
    }
}