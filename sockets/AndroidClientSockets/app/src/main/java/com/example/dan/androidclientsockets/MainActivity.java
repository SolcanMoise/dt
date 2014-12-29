package com.example.dan.androidclientsockets;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends ActionBarActivity {

    Socket socket = null;
    BufferedReader in;
    PrintWriter out;

    private Button connectBtn, sendBtn, closeBtn;
    private TextView textResponse, ip, port;

    private static final int SERVERPORT = 5000;
    private static final String SERVER_IP = "10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBtn = (Button) findViewById(R.id.connectButton);
        sendBtn = (Button) findViewById(R.id.sendButton);
        closeBtn = (Button) findViewById(R.id.closeButton);
        textResponse = (TextView) findViewById(R.id.textResponse);

        ip = (TextView) findViewById(R.id.ip);
        port = (TextView) findViewById(R.id.port);


        connectBtn.setOnClickListener(connectBtnOnClickListener);
        sendBtn.setOnClickListener(sendBtnOnClickListener);
        closeBtn.setOnClickListener(closeBtnOnClickListener);
    }

    View.OnClickListener connectBtnOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (socket == null) {
                try {
                    new Thread(new ClientThread()).start();
                    textResponse.append("Connection started\n");
                } catch (Exception ex) {

                }
            }
        }
    };

    View.OnClickListener sendBtnOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (socket != null) {
                try {
                    DataExchange dataExchange = new DataExchange();
                    dataExchange.execute();
                } catch (Exception ex) {

                }
            }
        }
    };

    View.OnClickListener closeBtnOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (socket != null) {
                try {
                    CloseDataExchange closeDataExchangeDataExchange = new CloseDataExchange();
                    closeDataExchangeDataExchange.execute();
                } catch (Exception ex) {

                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(ip.getText().toString());
                socket = new Socket(serverAddr, Integer.parseInt(port.getText().toString()));

                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    public class DataExchange extends AsyncTask<Void, Void, Void> {

        String response = "";

        DataExchange() {
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                EditText et = (EditText) findViewById(R.id.message);
                String message = et.getText().toString();

                    out.println("~C #" + message + " ~");
                    String str = in.readLine();
                    response += str+"\n";

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.append(response);
            super.onPostExecute(result);
        }
    }


    public class CloseDataExchange extends AsyncTask<Void, Void, Void> {

        String response = "";

        CloseDataExchange() {
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                //Send STOP message to Server
                out.println("STOP");
                response += " STOP";
                in.close();
                out.close();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.append(response);
            super.onPostExecute(result);
        }
    }
}
