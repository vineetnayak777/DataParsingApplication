package com.example.dataparsingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ViewActivity extends AppCompatActivity {

    int mode;
    TextView xmlPlaceHolder, jsonPlaceHolder, xmlHeading, jsonHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mode = getIntent().getIntExtra("mode",0);

        xmlPlaceHolder = findViewById(R.id.xmlPlaceHolder);
        jsonPlaceHolder = findViewById(R.id.jsonPlaceHolder);
        xmlHeading = findViewById(R.id.xmlHeading);
        jsonHeading = findViewById(R.id.JsonHeading);

        if(mode==1){
            parseXml();
        }

        if(mode == 2){
            parseJson();
        }
    }
    public void parseJson(){

        xmlPlaceHolder.setText(" ");
        xmlHeading.setText(" ");
        //Read file -> inputStream returns in byte
        //array = inputStream
        //String(array)
        String stringData = null;
        try {
            InputStream inputStream= getAssets().open("input.json");
            //Gives an Array Size
            int size = inputStream.available();
            byte buffer[] = new byte[size];
            //Stores an value to buffer
            inputStream.read(buffer);

            stringData = new String(buffer);

            //We need to convert the data into json format

            JSONObject jsonObject = new JSONObject(stringData);

            JSONObject cityObject = jsonObject.getJSONObject("City");
            String cityName = cityObject.getString("City-Name");
            String latitude = cityObject.getString("Latitude");
            String longitude = cityObject.getString("Longitude");
            String temperature = cityObject.getString("Temperature");
            String humidity = cityObject.getString("Humidity");

            jsonPlaceHolder.setText("City Name : "+cityName+"\n");
            jsonPlaceHolder.append("Longitude : "+longitude+"\n");
            jsonPlaceHolder.append("Latitude : "+latitude+"\n");
            jsonPlaceHolder.append("Temperature : "+temperature+"\n");
            jsonPlaceHolder.append("Humidity : "+humidity+"\n");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void parseXml(){
        xmlPlaceHolder.setText(" ");
        jsonPlaceHolder.setText(" ");
        jsonHeading.setText(" ");
        try {
            InputStream inputStream= getAssets().open("input.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(inputStream);

            //incase of multiple values document.normalize();

            NodeList cityList = document.getElementsByTagName("City");

            for(int i = 0; i < cityList.getLength(); i++){
                Node c = cityList.item(i);

                if(c.getNodeType()==Node.ELEMENT_NODE){
                    Element city = (Element) c;
                    //String id = city.getAttribute("id");

                    NodeList cityDetailList = city.getChildNodes();

                    for(int j=0; j<cityDetailList.getLength(); j++){
                        Node n = cityDetailList.item(j);

                        if(n.getNodeType()==Node.ELEMENT_NODE){
                            Element cityDetail = (Element) n;

                            String tagValue = cityDetail.getTagName();
                            String value = cityDetail.getTextContent();
                            xmlPlaceHolder.append(tagValue + "-" + value+"\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}