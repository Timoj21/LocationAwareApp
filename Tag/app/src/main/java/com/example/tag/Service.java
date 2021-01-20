package com.example.tag;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.osmdroid.util.GeoPoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class Service extends android.app.Service {

    private Socket serverSocket;
    private DataInputStream serverDis;
    private DataOutputStream serverDos;

    private Socket gameSocket;
    private DataInputStream gameDis;
    private DataOutputStream gameDos;


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
            this.serverSocket = new Socket(ip, 5056);

            // obtaining input and out streams
            this.serverDis = new DataInputStream(serverSocket.getInputStream());
            this.serverDos = new DataOutputStream(serverSocket.getOutputStream());

            if(Data.INSTANCE.getPlayer().equals("Tikker")) {

                Data.INSTANCE.setPlayerId(IDgenerator.generate());

                sendMessageToServer("MakeNewGame");

                //Game port
                String received = serverDis.readUTF();
                int receivedGameSocket = Integer.parseInt(received);
                this.gameSocket = new Socket(ip, receivedGameSocket);
                System.out.println("game socket van ontsnapper is" + receivedGameSocket);

                this.gameDis = new DataInputStream(gameSocket.getInputStream());
                this.gameDos = new DataOutputStream(gameSocket.getOutputStream());

                //Game pin
                String gameCode = serverDis.readUTF();

                Thread.sleep(2000);

                sendMessageToGame("Tikker" + ":" + Data.INSTANCE.getPlayerId() + ":" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());
                join("Tikker", Data.INSTANCE.getPlayerId());
            } else {
                Data.INSTANCE.setPlayerId(IDgenerator.generate());
                sendMessageToServer("JoinGame" + Data.INSTANCE.getGameId());

                String receivedPortNumber = serverDis.readUTF();
                if (!receivedPortNumber.equals("No Valid Port")) {
                    int receivedGameSocket = Integer.parseInt(receivedPortNumber);
                    this.gameSocket = new Socket(ip, receivedGameSocket);
                    System.out.println("game socket van ontsnapper is" + receivedGameSocket);
                    this.gameDis = new DataInputStream(gameSocket.getInputStream());
                    this.gameDos = new DataOutputStream(gameSocket.getOutputStream());

                    Thread.sleep(2000);

                    sendMessageToGame("Ontsnapper" + ":" + Data.INSTANCE.getPlayerId() + ":" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());
                    join("Ontsnapper", Data.INSTANCE.getPlayerId());
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
                System.out.println(Data.INSTANCE.getPlayer());
                sendMessageToGame("LastLocation" + ":" + typeOfPlayer + ":" + id + ":"
                        + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());

                System.out.println("DIT IS MIJN HUIDIGE LOCATIE" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());

                try {
                    String received = this.gameDis.readUTF();

                    switch (received) {
                        case "SENDING LOCATION":
                            String amountOfOntsnappers = this.gameDis.readUTF();
                            int amount = Integer.parseInt(amountOfOntsnappers);
                            for (int i = 0; i < amount; i++) {
                                String ontsnapperLocationReceived = this.gameDis.readUTF();
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
                                System.out.println(Data.INSTANCE.getGeoPoints().size());
                            }
                            break;

                        case "SENDING DISTANCE":
                            String amountOfOntsnappersForDistance = this.gameDis.readUTF();
                            int amountForDistance = Integer.parseInt(amountOfOntsnappersForDistance);
                            if (typeOfPlayer.equals("Tikker")) {
                                for (int i = 0; i < amountForDistance; i++) {
                                    //show distances
                                    String distanceBetweenID = this.gameDis.readUTF();
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
                            break;
                        default :
                            System.out.println("komt niet in de switch");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Thread.sleep(5000);
                }


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(String message) throws IOException {
        serverDos.writeUTF(message);
    }

    public void sendMessageToGame(String message) {
        System.out.println(Data.INSTANCE.getPlayer() + " sends message: " + message);

        try {
            gameDos.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
