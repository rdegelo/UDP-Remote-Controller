package com.technocreatives.rdegelo.creativeremotecontroller.executor;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.technocreatives.rdegelo.creativeremotecontroller.model.Command;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Sequence;
import com.technocreatives.rdegelo.creativeremotecontroller.model.Settings;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by rdegelo on 13/04/2017.
 */

public class SequenceExecutor {
    private Settings settings;
    private Sequence s;
    private  Handler h;

    public SequenceExecutor(Settings settings, Sequence s, Handler h) {
        this.settings = settings;
        this.s = s;
        this.h = h;
    }

    public void startExecutionAsync() {
        ExecuteSequenceAsyncTask task = new ExecuteSequenceAsyncTask();

        if (Build.VERSION.SDK_INT >= 11)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else task.execute();
    }

    private class ExecuteSequenceAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatagramSocket ds = null;
            DatagramPacket dp = null;

            try
            {
                ds = new DatagramSocket();

                for(Command c : s.getCommands()) {
                    postMessage("Waiting " + c.getDelayBefore() + "ms");
                    Thread.sleep(c.getDelayBefore());

                    postMessage("Sending: " + c.getText());

                    byte[] msg = (c.getText() + "\n").getBytes();
                    if(settings.isUse_broadcast()) {
                        ds.setBroadcast(true);

                        //Try default broadcast ip
                        try {
                            dp = new DatagramPacket(msg, msg.length, InetAddress.getByName("255.255.255.255"), settings.getPort());
                            ds.send(dp);
                        } catch (Exception e) {
                        }

                        //Try all interfaces
                        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                        while (interfaces.hasMoreElements()) {
                            NetworkInterface networkInterface = interfaces.nextElement();

                            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                                InetAddress broadcast = interfaceAddress.getBroadcast();
                                if (broadcast == null) {
                                    continue;
                                }

                                try {
                                    DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, broadcast, settings.getPort());
                                    ds.send(sendPacket);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    else {
                        InetAddress address = InetAddress.getByName(settings.getIp());
                        dp = new DatagramPacket(msg, msg.length, address, settings.getPort());
                        ds.send(dp);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (ds != null)
                {
                    ds.close();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            Message msg = new Message();
            Bundle bndl = new Bundle();
            bndl.putString("stop", "stop");

            msg.setData(bndl);
            h.sendMessage(msg);

            super.onPostExecute(result);
        }

        protected void postMessage(String text) {
            Message msg = new Message();
            Bundle bndl = new Bundle();
            bndl.putString("text", text);

            msg.setData(bndl);

            h.sendMessage(msg);
        }
    }
}
