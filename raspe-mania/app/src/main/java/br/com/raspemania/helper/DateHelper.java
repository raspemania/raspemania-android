package br.com.raspemania.helper;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private static DateFormat dateFormat  = new SimpleDateFormat("dd/MM/yyyy");
    private static Calendar c;

    public static Date addData(String dateString, int daysAdd, Context context){

        String date = dateString;
        c = Calendar.getInstance();

        try {
            c.setTime(dateFormat.parse(date));
            c.add(Calendar.DATE, daysAdd);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Ocorreu um erro inesperado!", Toast.LENGTH_LONG).show();
        }
        return c.getTime();
    }

    public static Date addData(Date date, int daysAdd){
        c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, daysAdd);
        return c.getTime();
    }

    public static Date stringToDate(String dateString, Context context)  {

        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Toast.makeText(context, "Ocorreu um erro inesperado!", Toast.LENGTH_LONG).show();
        }

        return date;
    }

    public static Date stringToDate(String dateString) throws Exception {

        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new Exception(e.getCause());
        }

        return date;
    }
}
