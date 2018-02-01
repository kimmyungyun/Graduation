package com.example.user.first;

import android.content.Intent;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Dot_Show extends AppCompatActivity {
    private FileReader fr = null;
    private GridView girdview;
    private DotShowAdapter adapter=null;
    FileInputStream fileInputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot__show);
        adapter = new DotShowAdapter();

        girdview = (GridView) findViewById(R.id.Dot_Show);
        Toast.makeText(getApplicationContext(), "읽는 것.", Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        //파일에 대한 모든 경로가 들어있음. (파일명까지)
        String File_Path = intent.getStringExtra("File_Path");
        //그리드 뷰 어댑터 붙여주기.
        girdview.setAdapter(adapter);
        int line;
        int jong,jung,cho;
        try {
            fileInputStream = new FileInputStream(File_Path);
            Reader in = new InputStreamReader(fileInputStream, "euc-kr");
            BufferedReader reader = new BufferedReader(in);

            while((line = reader.read()) != -1) {
                if (line == 32)    //띄어쓰기 일 경우 어떻게 처리할지 생각 해봐야할듯
                    ItemAdd((char)0b0," ");
                else if (line == 13 || line == 10) //엔터인 경우인데 둘이 같이 붙어다님. 생각해봐야할듯.
                    ItemAdd((char)0b0," ");
                else {
                    line = line - 0xAC00;
                    jong = line % 28;
                    jung = ((line - jong) / 28) % 21;
                    cho = (((line - jong) / 28) - jung) / 21;
                    Dot dot = new Dot(cho, jung, jong);

                    //초성일 경우 쓰기
                    switch (dot.whatcase / 6) {
                        case 0:
                            ItemAdd((char)dot.cb_cho1,dot.ch_cho);
                            break;
                        case 1:
                            break;
                        default:
                            //ItemAdd((char)dot.cb_cho1,dot.ch_cho);
                            // 18.02.01 13:43 박종수 dot.ch_cho-> string: " "로 하여 빈칸이 나오게
                            ItemAdd((char)dot.cb_cho1, " ");
                            ItemAdd((char)dot.cb_cho2,dot.ch_cho);
                            break;
                    }

                    //중성일 경우 쓰기
                    switch ((dot.whatcase % 6) / 3) {
                        case 0:
                            ItemAdd((char)dot.cb_jung1,dot.ch_jung);
                            break;
                        default:
                            ItemAdd((char)dot.cb_jung1,dot.ch_jung);
                            ItemAdd((char)dot.cb_jung2,dot.ch_jung);
                            break;
                    }

                    //종성일 경우 쓰기.
                    switch (dot.whatcase % 3) {
                        case 0:
                            ItemAdd((char)dot.cb_jong1,dot.ch_jong);
                            ItemAdd((char)dot.cb_jong2,dot.ch_jong);
                            break;
                        case 1:
                            ItemAdd((char)dot.cb_jong1,dot.ch_jong);
                            break;
                        default:
                            break;
                    }
                }
            }
                reader.close();
        }catch(Exception e){}


        /*
        int data;
        char ch;
        File file = new File(File_Path);
        try{
            fr=new FileReader(file);
            int j=0;
            while((data = fr.read()) != -1)
            {
                //글자 하나를 읽을 때마다 초기화.
                ch=(char)data;
                ItemAdd(ch);
            }
            //linearLayout.addView(parentLL2);
        }catch(Exception e){
            e.printStackTrace();
        }
        */
    }
    public void ItemAdd(char A, String Hangul){
        String Name="a";
        for(int i=5;i>=0;i--)
        {
            if((( A >> i) & 0b1) == 1)
                Name = Name+"1";
            else if((( A >> i) & 0b1)==0)
                Name = Name+"0";
        }
        adapter.addItem(ContextCompat.getDrawable(this,getResources().getIdentifier(Name, "drawable", this.getPackageName())),null, Hangul,1);
    }

}
