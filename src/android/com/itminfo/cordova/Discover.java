package com.itminfo.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 
 */
public class Discover extends CordovaPlugin {
    static final private int SERVICE_ID = 0x49AF5D2C;
    static final private int VERSION = 0x01;
    static final private int SERVICE_PORT = 7904;
        
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        
        if (action.equals("search")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try{
                        InetSocketAddress addr = findServer(cordova.getActivity().getApplicationContext());
                        if(addr == null){
                            callbackContext.error("Server not found");
                        }else{
                            callbackContext.success(addr.getHostString() + ":" + addr.getPort());
                        }
                    }catch(IOException e){
                        callbackContext.error(e.getMessage());
                    }
                }
            });
            return true;
        }
        
        return false;
    }

    public InetSocketAddress findServer(Context context) throws IOException
    {
        InetAddress broadcastAddr = getBroadcastAddress(context);

        DatagramSocket socket;
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(3000);

        ByteBuffer dataBuffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        dataBuffer.putInt(SERVICE_ID);
        dataBuffer.putInt(VERSION);

        DatagramPacket packet = new DatagramPacket(dataBuffer.array(), 8, broadcastAddr, SERVICE_PORT);

        socket.send(packet);

        socket.receive(packet);

        dataBuffer.flip();
        int receivedServiceID = dataBuffer.getInt();
        int receivedVersion = dataBuffer.getInt();

        if(receivedServiceID != SERVICE_ID || receivedVersion != VERSION)
            return null;

        return new InetSocketAddress(packet.getAddress(), SERVICE_PORT);
    }

    private InetAddress getBroadcastAddress(Context context) throws IOException 
    {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        byte[] ipQuads = new byte[4];
        for (int k = 0; k < 4; k++){
            ipQuads[k] = (byte) ((ip >> k * 8) & 0xFF);
        }

        InetAddress addr = Inet4Address.getByAddress(ipQuads);
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(addr);
        return networkInterface.getInterfaceAddresses().get(1).getBroadcast();
    }
}