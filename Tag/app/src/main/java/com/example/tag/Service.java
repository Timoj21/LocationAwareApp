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
import java.util.Random;

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
        //run();

        /*Runnable runnable = new Runnable() {
            public void run() {
                connectToServer();
            }
        };

        this.thread = new Thread(runnable);
        thread.start();*/

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    public void run(){


    }

    public double calculateDistance(String location1, String location2){
        String[] location1Array = location1.split(",");
        double lon1 = Double.parseDouble(location1Array[0]);
        double lat1 = Double.parseDouble(location1Array[1]);

        String[] location2Array = location2.split(",");
        double lon2 = Double.parseDouble(location2Array[0]);
        double lat2 = Double.parseDouble(location2Array[1]);



        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double distance;
        // Radius of earth in kilometers. Use 3956
        // for miles

        //double r = 3956;
        //distance = c * r;


        double r = 6371;
        distance = c * r;
        /*if (distance <= 0.01) {
            distance *= 1000;
        }*/

        return distance;
    }

    public void connectToServer() {
        try {
            Thread.sleep(2000);

            // getting localhost ip
            String ip = "192.168.178.33";

            // establish the connection with server port 5056
            this.serverSocket = new Socket(ip, 5056);

            // obtaining input and out streams
            this.serverDis = new DataInputStream(serverSocket.getInputStream());
            this.serverDos = new DataOutputStream(serverSocket.getOutputStream());

            if (Data.INSTANCE.getPlayer().equals("Tikker")) {

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

    public void join(String typeOfPlayer, String id) {
        try {
            while (true) {
                sendMessageToGame("LastLocation" + ":" + typeOfPlayer + ":" + id + ":"
                        + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());

                System.out.println("DIT IS MIJN HUIDIGE LOCATIE" + Data.INSTANCE.getLocation().getLongitude() + "," + Data.INSTANCE.getLocation().getLatitude());

                if (Data.INSTANCE.isTargetReached()) {
                    GeoPoint geoPoint = new GeoPoint(makeRandomGeoPoint());

                    sendMessageToGame("NewGeoPoint" + ":" + typeOfPlayer + ":" + id + ":"
                            + geoPoint.getLongitude() + "," + geoPoint.getLatitude());

                    try {
                        String S = this.gameDis.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    String received = this.gameDis.readUTF();

                    switch (received) {
                        case "SENDING LOCATION":
                            String ontsnapperLocationReceived = this.gameDis.readUTF();
                            String[] ontsnapperLocationArray = ontsnapperLocationReceived.split("~");
                            String ontsnapperLocation = ontsnapperLocationArray[0];
                            String ontsnapperID = ontsnapperLocationArray[1];
                            String[] ontsnapperCoor = ontsnapperLocation.split(",");
                            String ontsnapperLon = ontsnapperCoor[0];
                            String ontsnapperLat = ontsnapperCoor[1];
                            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(ontsnapperLat), Double.parseDouble(ontsnapperLon));
                            /*if (Data.INSTANCE.getGeoPoints().containsKey(ontsnapperID)) {
                                Data.INSTANCE.getGeoPoints().replace(ontsnapperID, Data.INSTANCE.getGeoPoints().get(ontsnapperID), geoPoint);
                            } else {
                                Data.INSTANCE.getGeoPoints().put(ontsnapperID, geoPoint);
                            }*/
                            System.out.println(Data.INSTANCE.getGeoPoints().size());

                            break;

                        case "SENDING DISTANCE":

                            //show distances
                            String distanceBetweenID = this.gameDis.readUTF();
                            Log.e(TAG, "join: JAJA: " + distanceBetweenID);
                            String[] distanceArray = distanceBetweenID.split(":");
                            String distanceID = distanceArray[0];
                            double distance = Double.parseDouble(distanceArray[1]);
                            String popupMessage = "Distance between you and " + distanceID + " is: " + distance + " km";
                            if (Data.INSTANCE.getDistances().containsKey(distanceID)) {
                                if (Data.INSTANCE.getDistances().get(distanceID) != distance) {
                                    Data.INSTANCE.getDistances().replace(distanceID, Data.INSTANCE.getDistances().get(distanceID), distance);
                                    Data.INSTANCE.setTargetReached(false);
                                }
                            } else {
                                Data.INSTANCE.getDistances().put(distanceID, distance);
                            }

                            Log.e(TAG, popupMessage);
                            //DistancePopup popup = new DistancePopup();
                            //popup.setText(popupMessage);



                                /*
                                als 3 min voorbij zijn, maak geopoint van locaties van ontsnappers en laat die op de map zien
                                */
                            break;
                        default:
                            System.out.println("komt niet in de switch");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Thread.sleep(1000);
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

    public GeoPoint makeRandomGeoPoint() {
        Random random = new Random();
        double lowLat = 51.57835075376575;
        double highLat = 51.594681317352745;

        double lowLon = 4.764797137382507;
        double highLon = 4.787660921269813;

        double randomLat = ((highLat - lowLat) * random.nextDouble()) + lowLat;
        double randomLon = ((highLon - lowLon) * random.nextDouble()) + lowLon;

        GeoPoint geoPoint = new GeoPoint(randomLat, randomLon);
        return geoPoint;
    }
}
