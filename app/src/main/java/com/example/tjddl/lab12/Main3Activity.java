package com.example.tjddl.lab12;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

public class Main3Activity extends AppCompatActivity {
    EditText editText_id, editText_pw;
    Button bt;
    TextView textView_login;
    String r;
    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            URL url = null;
            try {
                 url = new URL("http://jerry1004.dothome.co.kr/info/login.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpURLConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            httpURLConnection.setDoOutput(true);

            String userid = editText_id.getText().toString();
            String password = editText_pw.getText().toString();

            String postData = "userid=" + URLEncoder.encode(userid)
                    + "&password=" + URLEncoder.encode(password);

            try {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;
            try {
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String result = loginResult(inputStream);
            r = result;
            super.run();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        init();
        thread.start();

    }


    private String loginResult(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        String result = "";
        while (sc.hasNext()) {
            result = sc.nextLine();
        }
        return result;
    }

    public void init() {
        editText_id = (EditText) findViewById(R.id.editText_ID);
        editText_pw = (EditText) findViewById(R.id.editText_PW);
        textView_login = (TextView) findViewById(R.id.textView_login);
        bt = (Button) findViewById(R.id.button_Login);

    }


    public void onClick(View v) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (r.equals("FAIL"))
                    textView_login.setText("로그인에 실패했습니다.");
                else
                    textView_login.setText(r + "님 로그인 성공");
            }
        });

    }
}
