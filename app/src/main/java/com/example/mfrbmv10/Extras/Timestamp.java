package com.example.mfrbmv10.Extras;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timestamp {

    public Timestamp() {
    }

    public String obtenerFecha(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d/MMM/yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    public String obtenerHora(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        return simpleDateFormat.format(calendar.getTime());
    }

    public String obtenerLugar(){
        return "";
    }
}
