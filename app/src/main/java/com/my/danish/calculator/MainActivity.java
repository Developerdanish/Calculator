package com.my.danish.calculator;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

    private double result;
    private String display="";
    private String current_operator="";
    private int inverted_btn=0;
    private TextView result_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animate();
        result_textView = (TextView)findViewById(R.id.result_textView);;

    }

    private void animate(){
        Animation my_anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);
        Animation my_anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
        ((LinearLayout)findViewById(R.id.row_1)).startAnimation(my_anim1);
        ((LinearLayout)findViewById(R.id.row_2)).startAnimation(my_anim2);
        ((LinearLayout)findViewById(R.id.row_3)).startAnimation(my_anim1);
        ((LinearLayout)findViewById(R.id.row_4)).startAnimation(my_anim2);
        ((LinearLayout)findViewById(R.id.row_5)).startAnimation(my_anim1);
    }

    private double format(String s){
        double d;
        try{
            d = Double.parseDouble(s);
        }catch  (Exception e){
            d = 0.0;
        }
        return d;
    }

    private void update_screen(){
        if(display.length() > 10){
            result_textView.setTextSize(20);
        }
        result_textView.setText(display);
    }

    public void number_click(View v){
        if(inverted_btn != 0){
            invert_operator_color((TextView)v, 2);
        }
        TextView t = (TextView)v;
        display += t.getText().toString();
        update_screen();
        ((TextView)findViewById(R.id.clear_btn)).setText("C");
    }

    public void decimal_click(View v){
        if(!display.contains(".")){
            display+=".";
            update_screen();
        }
    }

    public void sign_click(View v){
        if(display.equals("") && current_operator.equals("") && result!=0){
            result = format("-"+result);
            result_textView.setText(""+result);
        }else if(!(result_textView.getText().toString()).contains("-")){
            display = "-"+display;
            update_screen();
        }else{
            display = display.substring(display.indexOf("-")+1, display.length());
            update_screen();
        }
    }

    private void invert_operator_color(TextView t, int key){

        if(key == 1){
            inverted_btn = t.getId();
            GradientDrawable background = (GradientDrawable)t.getBackground();
            background.setColor(ContextCompat.getColor(this, R.color.white));
            t.setTextColor(ContextCompat.getColor(this, R.color.dark_orange_button));
        }else{
            GradientDrawable background = (GradientDrawable)((TextView)findViewById(inverted_btn)).getBackground();
            background.setColor(ContextCompat.getColor(this, R.color.dark_orange_button));
            ((TextView)findViewById(inverted_btn)).setTextColor(ContextCompat.getColor(this, R.color.white));
        }


    }

    public void operator_click(View v){
        if(inverted_btn != 0){
            invert_operator_color(result_textView, 2);
        }
        if(current_operator != "" && result != 0 && !display.equals("")){
            result = format(operate(result, display, current_operator));
            result_textView.setText(""+result);
            current_operator = ((TextView)v).getText().toString();
            invert_operator_color(((TextView)v), 1);
            display = "";
        }else if(result!=0 && display.equals("")){
            current_operator = ((TextView)v).getText().toString();
            invert_operator_color(((TextView)v), 1);
            display = "";
        }else{
            current_operator = ((TextView)v).getText().toString();
            result = format(display);
            invert_operator_color(((TextView)v), 1);
            display = "";
        }

    }

    public void clear_click(View v){
        if(((TextView)v).getText().equals("C")){
            if(!display.equals(""))
                display = display.substring(0, display.length()-1);
                update_screen();
            if(display.equals("")){
                result_textView.setText(""+0);
                ((TextView)v).setText("AC");
            }
        }else{
            display = "";
            current_operator = "";
            result = 0;
            update_screen();
            result_textView.setText("0");
            if (inverted_btn != 0){
                invert_operator_color((TextView)v, 2);
            }
        }

    }

    private String operate(double a, String b, String operator){
        double res=0;
        int error=0;
        if(operator.equals("+")){
            res = a + format(b);
        }else if(operator.equals("-")){
            res = a - format(b);
        }else if(operator.equals("X")){
            res = a * format(b);
        }else if(operator.equals("^")){
            res = Math.pow(a, format(b));
        } else if(operator.equals("/")){
            try{
                res = a/format(b);
                System.out.println(""+a+"/"+b+"is"+res);
            }catch (Exception e){
                error = 1;
                System.out.println("error");
            }
        }else{
            return null;
        }
        if (error == 1){
            return "Error";
        }else{
            return ""+res;

        }
    }

    private double round_off(double d){
        return Math.round(d * 10000000.0)/10000000.0;
    }

    public void on_equal_click(View v){
        if (inverted_btn != 0){
            invert_operator_color((TextView)v, 2);
        }
        result_textView.setText(operate(result, display, current_operator));
        result = format(operate(result, display, current_operator));
        display="";
    }



}
