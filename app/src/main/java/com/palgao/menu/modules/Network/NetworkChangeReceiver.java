//package com.palgao.menu.modules.Network;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.widget.Toast;
//
//public class NetworkChangeReceiver extends BroadcastReceiver {
//    private ConnectionListener listener;
//
//    public NetworkChangeReceiver(ConnectionListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        boolean isConnected = NetworkUtils.isInternetAvailable(context);
//        listener.onNetworkChanged(isConnected);
//        if (!isConnected) {
//            Toast.makeText(context, "Se perdió la conexión a Internet", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public interface ConnectionListener {
//        void onNetworkChanged(boolean isConnected);
//    }
//}
//
