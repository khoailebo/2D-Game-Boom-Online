package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.Duration;

public class Client implements Runnable {
    Thread ConnectThread;
    private Socket connection;
    private ConnectionListener connectionListener;

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    PrintWriter writer;
    BufferedReader reader;
    private boolean Connected;

    public boolean isConnected() {
        return Connected;
    }

    public void setConnected(boolean connected) {
        Connected = connected;
        if (connectionListener != null) {
            connectionListener.onConnect();
        }
    }

    public int ClientId = 0;
    HashMap<String, InvokeLater> ListenerList = new HashMap<>();
    HashMap<String, ArrayList<InvokeLater>> CallBackList = new HashMap<>();
    public final String GetTimeDelayEvent = "GetTimeDelayed";
    public static Client clientInstance;

    private Client() {
    };

    public static Client getInstance() {
        if (clientInstance == null) {
            clientInstance = new Client();
        }
        return clientInstance;
    }

    public void close() {
        try {
            connection.close();
            setConnected(false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    long time = 0;

    /**
     * Get delayed time for a packet to go from
     * client to server and comeback
     * 
     * @param showTime boolean variable indicate if you want
     *                 to println the delayedTime
     * @return The delayedTime in miliseconds
     * 
     */
    public long getDelayedTime(boolean showTime) {
        Instant start = Instant.now();

        sendEvent(GetTimeDelayEvent, null, new InvokeLater() {

            @Override
            public void call(Object... os) {
                Instant end = Instant.now();
                Duration responseTime = Duration.between(start, end);
                time = responseTime.toMillis();
            }

        });
        if (showTime)
            System.out.println("Delayed time: " + time + " milisecs");
        return time;
    }

    public void connectToServer(String serverIPAdd, int serverPort) {
        if (!isConnected()) {
            try {
                // connection = new Socket("0.tcp.ap.ngrok.io", 12149);
                // connection = new Socket("localhost", 5000);
                connection = new Socket(serverIPAdd, serverPort);
                setConnected(true);
                writer = new PrintWriter(connection.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ConnectThread = new Thread(this, "ListentoServer");
                ConnectThread.setPriority(Thread.MAX_PRIORITY);
                ConnectThread.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void on(String eventName, InvokeLater callback) {
        ListenerList.put(eventName, callback);
    }

    /**
     * Send a event to server, the event
     * must be installed on server before sent
     * 
     * @param eventName the name of the event
     * @param data      the data to send to server
     * @param callback  the call function inside this object
     *                  will be call
     *                  if server sendback response data
     */
    public void sendEvent(String eventName, String data, InvokeLater callback) {
        sendMsg(eventName, new StringBuffer(eventName).append("|").append(data).toString(), callback);
    }

    public void sendMsg(String callbackKey, String msg, InvokeLater callback) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("send: " + msg);
                if (writer != null) {
                    writer.println(msg);
                }
                if (callback != null) {
                    if (!CallBackList.containsKey(callbackKey)) {
                        CallBackList.put(callbackKey, new ArrayList<InvokeLater>());
                    }
                    ArrayList<InvokeLater> callBackOnKey = CallBackList.get(callbackKey);
                    callBackOnKey.add(callback);
                }
            }

        }).start();
    }

    @Override
    public void run() {
        String message;
        try {
            while (true) {
                message = readMsg();
                if (message != null) {
                    System.out.println("read: " + message);
                    performCallBack(message);
                    performListener(message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Lose connect to serverr");
            ex.printStackTrace();
        }
    }

    public void performCallBack(String msg) throws InterruptedException {
        String[] buffers = msg.split("\\|");
        String eventName = buffers[0];
        String data = buffers[1];
        if (CallBackList.containsKey(eventName)) {
            ArrayList<InvokeLater> callBackListOnKey = CallBackList.get(eventName);
            if (callBackListOnKey.size() > 0) {
                InvokeLater callback = callBackListOnKey.remove(0);
                callback.call((Object) data);
            }
            if (callBackListOnKey.size() == 0) {
                CallBackList.remove(eventName);
            }
        }
    }

    public void performListener(String msg) throws InterruptedException {
        String[] buffers = msg.split("\\|");
        String eventName = buffers[0];
        String data = buffers[1];
        if (ListenerList.containsKey(eventName)) {
            InvokeLater callback = ListenerList.get(eventName);
            callback.call((Object) data);
        }
    }

    public String readMsg() throws IOException {
        String msg = null;
        msg = reader.readLine();
        return msg;
    }
}
