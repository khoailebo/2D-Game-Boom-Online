package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.SocketException;
import dtos.PlayerDTO;
import java.awt.Color;

import ui.LogArea;
import ui.event.InvokeLater;
import ui.event.PublicEvent;
import utility.GsonHelper;

public class Server {
    ServerSocket serverSocket;
    ArrayDeque<Client> ClientList;
    ArrayDeque<Integer> IdList;
    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    int port = 5000;
    public HashMap<String, InvokeLater> ListenerList = new HashMap<>();
    public static Server instance;

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private Server() {
        ClientList = new ArrayDeque<>();
        IdList = new ArrayDeque<>();
        for(int i =0;i < 4;i++)
        {
            IdList.add(i);
        }
        setUpEvent();
    }

    public void setUpEvent() {
        ListenerList.put("UpdateState", new InvokeLater() {

            @Override
            public void call(Object... os) {
                // TODO Auto-generated method stub
                Client currentClient = (Client) os[0];
                currentClient.tank = GsonHelper.<PlayerDTO>fromJsonToObj((String) os[1], PlayerDTO.class);
                currentClient.sendEvent("UpdateState", GsonHelper.toJsonObj(currentClient.tank));
                for (Client client : ClientList) {
                    if (client.ClientId != currentClient.ClientId) {
                        client.sendEvent("UpdateOpponentLocation", GsonHelper.toJsonObj(currentClient.tank),currentClient.ClientId);
                    }
                }
            }

        });
        ListenerList.put("GetLocation", new InvokeLater() {

            @Override
            public void call(Object... os) {
                // TODO Auto-generated method stub
                Client client = (Client)os[0];
                client.sendEvent("GetLocation", GsonHelper.toJsonObj(client.tank));
            }
            
        });
        ListenerList.put("Disconnect", new InvokeLater() {

            @Override
            public void call(Object... os) {
                Client currentClient = (Client) os[0];
                currentClient.disconnectToServer();
            }

        });
        ListenerList.put("AddBoom", new InvokeLater() {

            @Override
            public void call(Object... os) {
                Client client = (Client) os[0];
                client.sendEvent("AddBoom", null);
                for (Client c : ClientList) {
                    if (c.ClientId != client.ClientId) {
                        c.sendEvent("OpponentAddBoom", null,client.ClientId);
                    }
                }
            }

        });
        ListenerList.put("GetTimeDelayed", new InvokeLater() {

            @Override
            public void call(Object... os) {
                Client client = (Client) os[0];
                client.sendEvent("GetTimeDelayed", null);
            }

        });

    }

    public void resetServer() {
        try {
            serverSocket.close();
            closeAllSocket();
            setRunning(false);
            // Update GUI server state
            PublicEvent.getInstance().getServerEvent().setServerState(false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            PublicEvent.getInstance().getLogEvent().logMsg("Reseting");
        }

    }

    public void sendAll() {

    }
    public void closeAllSocket(){
        int numOfClients = ClientList.size();
        for(int i = 0;i < numOfClients;i++)
        {
            Client c = ClientList.peek();
            try {
                c.sendEvent("ServerClosed", null);
                // c.close();
                c.disconnectToServer();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            if (isRunning()) {
                serverSocket.close();
                closeAllSocket();
                setRunning(false);
                // Update GUI server state
                PublicEvent.getInstance().getServerEvent().setServerState(false);
            } else {
                PublicEvent.getInstance().getLogEvent().logMsg("Server hasn't been runned!");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            PublicEvent.getInstance().getLogEvent().logMsg("Server hasn't been runned!");
        }
    }

    public void run() {
        if (isRunning()) {
            PublicEvent.getInstance().getLogEvent().logMsg("Server is already running!");
        } else {
            Thread serverThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        serverSocket = new ServerSocket(port);
                        PublicEvent.getInstance().getLogEvent().logMsg("Server is running at port: " + port,
                                LogArea.SUCCESS);
                        setRunning(true);
                        // Update GUI server state
                        PublicEvent.getInstance().getServerEvent().setServerState(true);

                        while (true) {
                            if (ClientList.size() >= 4) {
                                serverSocket.close();
                            }
                            Socket client = serverSocket.accept();
                            synchronized (ClientList) {
                                Client c = new Client(client, Server.this);
                                onConnected(c);
                                ClientList.add(c);
                            }
                            PublicEvent.getInstance().getLogEvent().logMsg("A Client connect", Color.MAGENTA);
                        }
                    } catch (SocketException ex) {

                        // PublicEvent.getInstance().getLogEvent().logMsg("A client move server!");
                        PublicEvent.getInstance().getLogEvent().logMsg("Server stop!", LogArea.WARNING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            serverThread.setName("Server Thread");
            serverThread.start();
        }

    }

    public void onConnected(Client c) {
        for (Client client : ClientList) {
            {
                client.sendEvent("SetOpponentLocation", GsonHelper.toJsonObj(c.tank),c.ClientId);
                c.sendEvent("SetOpponentLocation", GsonHelper.toJsonObj(client.tank),client.ClientId);
            }
        }
    }
}
