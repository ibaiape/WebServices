package com.example.dm2.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    private Button btObetener;
    private EditText longitud;
    private EditText deMedida;
    private EditText aMedida;
    private String resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btObetener = (Button)findViewById(R.id.btObtener);
        longitud =(EditText)findViewById(R.id.longitud);
        deMedida =(EditText)findViewById(R.id.deMedida);
        aMedida =(EditText)findViewById(R.id.aMedida);
        btObetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String longitud = MainActivity.this.longitud.getText().toString();
                String deMed = MainActivity.this.deMedida.getText().toString();
                String aMed = MainActivity.this.aMedida.getText().toString();
                AsyncPost task = new AsyncPost();
                task.execute(longitud, deMed, aMed);
            }
        });
    }
    private class AsyncPost extends AsyncTask <String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                HttpURLConnection conn;
                URL url = new URL("http://www.webserviceX.NET/length.asmx/ChangeLengthUnit");
                String param ="LengthValue="+ URLEncoder.encode(params[0],"UTF-8");
                param += "&fromLengthUnit="+URLEncoder.encode(params[1],"UTF-8");
                param += "&toLengthUnit="+URLEncoder.encode(params[2],"UTF-8");
                conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(param.getBytes().length);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(param);
                out.close();
                String result ="";
                resultado ="";

                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine()) {
                    result = inStream.nextLine();
                    if (result.indexOf("double") > 0)
                        resultado = result.replace
                                ("<double xmlns=\"http://www.webserviceX.NET/\">", "").replace("</double>", "");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(MainActivity.this, "Resultado: "+resultado,Toast.LENGTH_LONG).show();
        }
    }



}