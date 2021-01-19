package com.example.tag;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.osmdroid.util.GeoPoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class Service extends android.app.Service {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private Thread thread;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Runnable runnable = new Runnable(){
            public void run(){
                connectToServer();
            }
        };

        this.thread = new Thread(runnable);
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void connectToServer(){
        try {
            Thread.sleep(2000);

            // getting localhost ip
            String ip = "192.168.178.33";

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams
            this.dis = new DataInputStream(s.getInputStream());
            this.dos = new DataOutputStream(s.getOutputStream());

            if(Data.INSTANCE.getPlayer().equals("Tikker")) {

                Data.INSTANCE.setGameId(IDgenerator.generate());

                sendMessageToServer("MakeNewGame");

                String received = dis.readUTF();

                int receivedGameSocket = Integer.parseInt(received);
                String gameCode = dis.readUTF();

                Socket gameSocket = new Socket(ip, receivedGameSocket);

                Thread.sleep(2000);

                sendMessageToServer("Tikker" + ":" + Data.INSTANCE.getGameId() + ":" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());
                join("Tikker", Data.INSTANCE.getGameId());
            } else {
                dos.writeUTF("JoinGame" + Data.INSTANCE.getGameId());

                String receivedPortNumber = dis.readUTF();
                if (!receivedPortNumber.equals("No Valid Port")) {
                    int gamePort = Integer.parseInt(receivedPortNumber);
                    Socket gameSocket = new Socket(ip, gamePort);

                    Thread.sleep(2000);

                    sendMessageToServer("Ontsnapper" + ":" + Data.INSTANCE.getGameId() + ":" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());
                    join("Ontsnapper", Data.INSTANCE.getGameId());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void join(String typeOfPlayer, String id){
        try {
            Thread.sleep(1000);
            while (true) {
                sendMessageToServer("LastLocation" + ":" + typeOfPlayer + ":" + id + ":"
                        + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());

                System.out.println("DIT IS MIJN HUIDIGE LOCATIE" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());

                try {
                    String received = this.dis.readUTF();

                    switch (received) {
                        case "SENDING LOCATION":
                            String amountOfOntsnappers = this.dis.readUTF();
                            int amount = Integer.parseInt(amountOfOntsnappers);
                            for (int i = 0; i < amount; i++) {
                                String ontsnapperLocationReceived = this.dis.readUTF();
                                String[] ontsnapperLocationArray = ontsnapperLocationReceived.split("~");
                                String ontsnapperLocation = ontsnapperLocationArray[0];
                                String ontsnapperID = ontsnapperLocationArray[1];
                                String[] ontsnapperCoor = ontsnapperLocation.split(",");
                                String ontsnapperLon = ontsnapperCoor[0];
                                String ontsnapperLat = ontsnapperCoor[1];
                                GeoPoint geoPoint = new GeoPoint(Double.parseDouble(ontsnapperLat), Double.parseDouble(ontsnapperLon));
                                if (Data.INSTANCE.getGeoPoints().containsKey(ontsnapperID)) {
                                    Data.INSTANCE.getGeoPoints().replace(ontsnapperID, Data.INSTANCE.getGeoPoints().get(ontsnapperID), geoPoint);
                                } else {
                                    Data.INSTANCE.getGeoPoints().put(ontsnapperID, geoPoint);
                                }
                            }
                            break;

                        case "SENDING DISTANCE":
                            String amountOfOntsnappersForDistance = this.dis.readUTF();
                            int amountForDistance = Integer.parseInt(amountOfOntsnappersForDistance);
                            if (typeOfPlayer.equals("Tikker")) {
                                for (int i = 0; i < amountForDistance; i++) {
                                    //show distances
                                    String distanceBetweenID = this.dis.readUTF();
                                    double distance = Double.parseDouble(distanceBetweenID);
                                    String popupMessage = "Distance between you and a player is: " + distance + " km";
                                    Log.e(TAG, popupMessage);
                                    //DistancePopup popup = new DistancePopup();
                                    //popup.setText(popupMessage);
                                }


                                /*
                                als 3 min voorbij zijn, maak geopoint van locaties van ontsnappers en laat die op de map zien
                                */

                            }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Thread.sleep(5000);
                }


            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(String message) throws IOException {
        dos.writeUTF(message);
    }
}
