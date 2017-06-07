package com.example.tjddl.lab12;

import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.w3c.dom.DOMException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Main2Activity extends AppCompatActivity {
    Button bt;
    Handler handler = new Handler();

    class myThread extends Thread{
        @Override
        public void run() {
            try{
                URL url = new URL("https://news.google.com/news/section?cf=all&hl=ko&pz=1&ned=kr&topic=m&output=rss");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK){
                    int itemCount = readData(urlConnection.getInputStream());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                urlConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

    ListView listView ;
    ArrayList<String> newsArr;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = (ListView)findViewById(R.id.listview);
        bt = (Button)findViewById(R.id.button);
        newsArr = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,newsArr);
        listView.setAdapter(adapter);

    }
        Thread th = new myThread();
    public void onClick(View v){
        th.start();
    }

    int readData(InputStream is) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(is);
            int datacount = parseDocument(document);

            return datacount;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    int parseDocument(Document document){
        Element docEle = document.getDocumentElement();
        NodeList nodeList = docEle.getElementsByTagName("item");
        int count = 0;
        if( (nodeList != null) && nodeList.getLength() >0 ) {
            for(int i = 0 ; i<nodeList.getLength() ; i++){
                String newsItem = getTagData(nodeList, i);

                if(newsItem != null) {
                    newsArr.add(newsItem);
                    count++;
                }
            }
        }
        return count;
    }

    private String getTagData(NodeList nodeList, int index) {
        String newsItem = null;
        try{
            Element entry = (Element)nodeList.item(index);
            Element title =(Element)entry.getElementsByTagName("title").item(0);
            Element pubDate =(Element)entry.getElementsByTagName("pubDate").item(0);

            String titleValue = null;
            if(title != null){
                Node firstChild = title.getFirstChild();
                if(firstChild != null) titleValue = firstChild.getNodeValue();
            }
            String pubDateValue = null;
            if(title != null){
                Node firstChild = pubDate.getFirstChild();
                if(firstChild != null) pubDateValue = firstChild.getNodeValue();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
            Date date = new Date();
            newsItem = titleValue + "-" + simpleDateFormat.format(date.parse(pubDateValue));
        }
        catch (DOMException e ){
            e.printStackTrace();
        }

        return newsItem;
    }


}
