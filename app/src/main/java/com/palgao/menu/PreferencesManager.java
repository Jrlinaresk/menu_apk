package com.palgao.menu;

import android.content.Context;

/**
 * Created by kama on 22/05/2017.
 */

public class PreferencesManager { //este si
    Context context;

    public PreferencesManager(Context context) {
        this.context = context;
    }

    public static PreferencesManager fromContext(Context context) {
        return new PreferencesManager(context);
    }

    public String baseUrl() {
        return Preferences.readSharedPreference(context, "baseUrl", "");
    }

    public void baseUrl(String value) {
        Preferences.writeSharedPreference(context, "baseUrl", value);
    }

    public String userEmail() {
        return Preferences.readSharedPreference(context, "userEmail", "");
    }

    public void userEmail(String value) {
        Preferences.writeSharedPreference(context, "userEmail", value);
    }
    public String userPassword() {
        return Preferences.readSharedPreference(context, "userPassword", "");
    }
    public void userPassword(String value) {
        Preferences.writeSharedPreference(context, "userPassword", value);
    }

    public String token() {
        return Preferences.readSharedPreference(context, "token", "");
    }

    public void token(String value) {
        Preferences.writeSharedPreference(context, "token", value);
    }

    public boolean rememberLogin() {
        return Preferences.readSharedPreference(context, "rememberLogin", false);
    }

    public void rememberLogin(boolean value) {
        Preferences.writeSharedPreference(context, "rememberLogin", value);
    }

    //Trip routes

    public String driver_client_route() {
        return Preferences.readSharedPreference(context, "driver_client_route", "");
    }

    public void driver_client_route(String value) {
        Preferences.writeSharedPreference(context, "driver_client_route", value);
    }

    public String client_destiny_route() {
        return Preferences.readSharedPreference(context, "client_destiny_route", "");
    }

    public void client_destiny_route(String value) {
        Preferences.writeSharedPreference(context, "client_destiny_route", value);
    }

    //Trip started data

    public float tripDistance() {
        return Preferences.readSharedPreference(context, "tripDistance", 0f);
    }

    public void tripDistance(float value) {
        Preferences.writeSharedPreference(context, "tripDistance", value);
    }

    public float tripCost() {
        return Preferences.readSharedPreference(context, "tripCost", 0f);
    }

    public void tripCost(float value) {
        Preferences.writeSharedPreference(context, "tripCost", value);
    }

    public long tripTime() {
        return Preferences.readSharedPreference(context, "tripTime", 0L);
    }

    public void tripTime(long value) {
        Preferences.writeSharedPreference(context, "tripTime", value);
    }

    public void requestTripId(long value) {
        Preferences.writeSharedPreference(context, "requestTripId", value);
    }

    public int requestTripId() {
        return Preferences.readSharedPreference(context, "requestTripId", -1);
    }

    public String userId() {
        return Preferences.readSharedPreference(context, "userId", "");
    }

    public void userId(String value) {
        Preferences.writeSharedPreference(context, "userId", value);
    }

    public void user_image_Id(String value) {
        Preferences.writeSharedPreference(context, "finlenameOnServer", value);
    }
    public String user_image_Id() {
        return Preferences.readSharedPreference(context, "finlenameOnServer", "");
    }
}
