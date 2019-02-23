package com.example.uitest;


import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.lang.Thread;
import android.content.BroadcastReceiver;





public class BluetoothConnectCLient extends Thread{
  private class ConnectThread extends Thread {
  private final BluetoothSocket mmSocket;
  private final BluetoothDevice mmDevice;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ...

    // Register for broadcasts when a device is discovered.
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(receiver, filter);
  }

  // Create a BroadcastReceiver for ACTION_FOUND.
  private final BroadcastReceiver receiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
          }
      }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ...

    // Don't forget to unregister the ACTION_FOUND receiver.
    unregisterReceiver(receiver);
  }

  public ConnectThread(BluetoothDevice device) {
      // Use a temporary object that is later assigned to mmSocket
      // because mmSocket is final.
      BluetoothSocket tmp = null;
      mmDevice = device;

      try {
          // Get a BluetoothSocket to connect with the given BluetoothDevice.
          // MY_UUID is the app's UUID string, also used in the server code.
          tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
      } catch (IOException e) {
          Log.e(TAG, "Socket's create() method failed", e);
      }
      mmSocket = tmp;
  }

  public void run() {
      // Cancel discovery because it otherwise slows down the connection.
      bluetoothAdapter.cancelDiscovery();

      try {
          // Connect to the remote device through the socket. This call blocks
          // until it succeeds or throws an exception.
          mmSocket.connect();
      } catch (IOException connectException) {
          // Unable to connect; close the socket and return.
          try {
              mmSocket.close();
          } catch (IOException closeException) {
              Log.e(TAG, "Could not close the client socket", closeException);
          }
          return;
      }

      // The connection attempt succeeded. Perform work associated with
      // the connection in a separate thread.
      manageMyConnectedSocket(mmSocket);
  }

  // Closes the client socket and causes the thread to finish.
  public void cancel() {
      try {
          mmSocket.close();
      } catch (IOException e) {
          Log.e(TAG, "Could not close the client socket", e);
      }
  }
}
}
