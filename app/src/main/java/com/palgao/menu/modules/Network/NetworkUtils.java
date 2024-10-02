//package com.palgao.menu.modules.Network;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkCapabilities;
//import android.os.Build;
//
//public class NetworkUtils {
//    public static boolean isInternetAvailable(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//                return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
//            } else {
//                // Compatibilidad con versiones antiguas de Android
//                android.net.NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//            }
//        }
//        return false;
//    }
//}
