package com.palgao.menu.modules.Bussiness.entityes;

import androidx.room.Entity;

@Entity
public class Horario {
    private int hora_open;
    private int hora_close;

    public int getHora_open() {
        return hora_open;
    }

    public void setHora_open(int hora_open) {
        this.hora_open = hora_open;
    }

    public int getHora_close() {
        return hora_close;
    }

    public void setHora_close(int hora_close) {
        this.hora_close = hora_close;
    }
}