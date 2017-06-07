package com.example.tjddl.lab12;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText et;
    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.etmsg);

    }


        class  myThread extends Thread {
            @Override
            public void run() {

                try {
                    URL url = new URL("http://www.google.com/");
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    String data = "";
                    InputStream is = null;

                    is = new BufferedInputStream(urlConnection.getInputStream());

                    Scanner s = new Scanner(is);
                    while (s.hasNext()) data += s.nextLine() + "\n";
                    s.close();
                    final String finalData = data;
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            et.setText(finalData);
                        }
                    });
                    urlConnection.disconnect();
                    super.run();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

    public void onClick(View v) {
        myThread th= new myThread();
        th.start();
    }
}
