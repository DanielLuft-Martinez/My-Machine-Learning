package com.example.glassbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.glassbox.R;

public class ShowSelectedDataActivity extends Activity {

    TextView txtText_fruit;
    TextView txtText_ball;
    TextView txtText_dog;
    TextView txtText_cat;
    TextView txtText_other_animals;
    TextView txtText_object;
    TextView txtText_vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_selected_data);

        //UI 객체생성
        txtText_fruit = (TextView)findViewById(R.id.txtText_fruit);
        txtText_ball = (TextView)findViewById(R.id.txtText_ball);
        txtText_dog = (TextView)findViewById(R.id.txtText_dog);
        txtText_cat = (TextView)findViewById(R.id.txtText_cat);
        txtText_other_animals = (TextView)findViewById(R.id.txtText_other_animals);
        txtText_object = (TextView)findViewById(R.id.txtText_object);
        txtText_vehicle= (TextView)findViewById(R.id.txtText_vehicle);

        //데이터 가져오기
        Intent intent = getIntent();
        String data_fruit = intent.getStringExtra("FRUIT");
        String data_ball = intent.getStringExtra("BALL");
        String data_dog = intent.getStringExtra("Dog");
        String data_cat = intent.getStringExtra("Cat");
        String data_other_animals = intent.getStringExtra("OTHER ANIMALS");
        String data_object = intent.getStringExtra("OBJECT");
        String data_vehicle = intent.getStringExtra("VEHICLE");
        txtText_fruit.setText(data_fruit);
        txtText_ball.setText(data_ball);
        txtText_dog.setText(data_dog);
        txtText_cat.setText(data_cat);
        txtText_other_animals.setText(data_other_animals);
        txtText_object.setText(data_object);
        txtText_vehicle.setText(data_vehicle);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}