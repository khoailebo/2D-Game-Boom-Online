package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import dtos.PlayerDTO;
import ui.LogArea;
import ui.event.InvokeLater;
import ui.event.PublicEvent;
import utility.GameController;
import utility.GsonHelper;

public class Client implements Runnable {
    public Socket ClientSocket;
    public Server server;
    public BufferedReader reader;
    public PrintWriter writer;
    public int ClientId;
    public PlayerDTO tank;

    Thread ClientThread;

    public Client(Socket socket, Server s) {
        this.server = s;
        this.ClientSocket = socket;
        this.ClientId = this.server.IdList.poll();
        try {
            reader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            writer = new PrintWriter(ClientSocket.getOutputStream(), true);
            setUpPlayer();
            ClientThread = new Thread(this);
            ClientThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUpPlayer() {
        sendEvent("SetClientId", ClientId);
        tank = GameController.getRandomState(ClientId);
        // sendEvent("SetLocation", GsonHelper.toJsonObj(tank));
    }

    public void sendEvent(String eventName, Object data) {
        sendMsg(new StringBuilder(eventName).append("|")
                .append(data).toString());
    }

    public void sendEvent(String eventName, Object data, int opponentId) {
        sendMsg(new StringBuilder(eventName).append("|")
                .append(data).append("_").append(opponentId).toString());
    }

    public void sendMsg(String msg) {
        writer.println(msg);
    }

    public void close() throws IOException {
        ClientSocket.close();
    }

    @Override
    public void run() {
        String message;
        try {
            while (true) {
                message = reader.readLine();
                if (message != null) {
                    PublicEvent.getInstance().getLogEvent().logMsg("read: " + message, LogArea.MESSAGE);
                    performListener(message);
                }

            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        } finally {
            disconnectToServer();
        }
    }

    public void disconnectToServer() {
        synchronized (server.ClientList) {
            if (server.ClientList.contains(this)) {
                GameController.counter--;
                server.IdList.add(ClientId);
                server.ClientList.remove(this);
                PublicEvent.getInstance().getLogEvent().logMsg("Client: " + ClientId + " move server");
                PublicEvent.getInstance().getLogEvent()
                        .logMsg("Number of client left: " + server.ClientList.size() + "\n");
                try {
                    close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void performListener(String msg) {
        String[] buffers = msg.split("\\|");
        String eventName = buffers[0];
        String data = "";
        if (buffers.length > 1) {
            data = buffers[1];
        }
        if (server.ListenerList.containsKey(eventName)) {
            InvokeLater callback = server.ListenerList.get(eventName);
            callback.call(this, (Object) data);
        }
    }

}
