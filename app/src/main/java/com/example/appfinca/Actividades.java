package com.example.appfinca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Actividades {

    //Contiene el singleton
    private static Actividades actividades;

    //Contiene el contexto
    private static Activity contexto;

    //Contiene la actividad con la que se va a trabajar
    private static Class actividad;

    //Parámetros para pasar entre actividades
    public static String        PARAM_1 = "PARAM_1";
    final public static int     PARAM_INT_1 = 1;
    public static String        PARAM_2 = "PARAM_2";
    public static String        PARAM_3 = "PARAM_3";
    public static String        PARAM_4 = "PARAM_4";
    public static String        PARAM_5 = "PARAM_5";
    public static String        PARAM_6 = "PARAM_6";
    public static String        PARAM_7 = "PARAM_7";
    public static String        PARAM_8 = "PARAM_8";
    public static String        PARAM_9 = "PARAM_9";
    public static String        PARAM_10 = "PARAM_10";
    public static double        PARAM_DOUBLE_1 = 0.0;
    public static long          PARAM_LONG = 0;

    //Constructor privado
    private Actividades()
    {

    }

    //Obtiene el singleton
    final public static Actividades getSingleton(final Context contexto, final Class actividad)
    {

        //Si ya hay instancia entonces
        if(Actividades.actividades == null)
            Actividades.actividades = new Actividades();

        //Guarda el contexto con el que debe de trabajar
        Actividades.contexto = (Activity) contexto;

        //Guarda la actividad con la que se va a trabajar
        Actividades.actividad  = actividad;

        //Devuelve la instancia única
        return Actividades.actividades;

    }


    //Muestra la actividad
    final public void muestraActividadRestrasada(final boolean cierraActividadActual, final int milisegundos)
    {
        //Corre el thread separado
        (new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(milisegundos);
                }
                catch(final Exception excepcion)
                {
                }

                //Si tiene que cerrar la actividad actual entonces que así sea
                if(cierraActividadActual)
                    Actividades.contexto.finish();

                //Corre la otra actividad
                Actividades.contexto.startActivity(new Intent(Actividades.contexto, Actividades.actividad));
            }

        }).start();
    }


    //Muestra la actividad
    final public void muestraActividad()
    {
        Actividades.contexto.startActivity(new Intent(Actividades.contexto, Actividades.actividad));
    }


    //Muestra la actividad
    final public void muestraActividadForResult(final int code)
    {
        Actividades.contexto.startActivityForResult(new Intent(Actividades.contexto, Actividades.actividad), code);
    }


    //Muestra la actividad
    final public void muestraActividad(final HashMap<String, String> listaStrings)
    {
        //Crea el intent
        Intent intent = new Intent(Actividades.contexto, Actividades.actividad);

        //Recorre todos los parámetros que tiene que pasar
        Iterator iterator = listaStrings.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            String value = (String) pair.getValue();
            intent.putExtra(key, value);
        }

        //Corre la actividad
        Actividades.contexto.startActivity(intent);
    }


    //Muestra la actividad
    final public void muestraActividad(final HashMap<String, String> listaStrings, final HashMap<String, Serializable> listaSerializable)
    {
        //Crea el intent
        Intent intent = new Intent(Actividades.contexto, Actividades.actividad);

        //Recorre todos los parámetros que tiene que pasar de strings
        Iterator iterator = listaStrings.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            String value = (String) pair.getValue();
            intent.putExtra(key, value);
        }

        //Recorre todos los parámetros que tiene que pasar de serializables
        iterator = listaSerializable.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            Serializable value = (Serializable) pair.getValue();
            intent.putExtra(key, value);
        }

        //Corre la actividad
        Actividades.contexto.startActivity(intent);
    }


    //Muestra la actividad
    final public void muestraActividadSerializable(final HashMap<String, Serializable> listaSerializable)
    {
        //Crea el intent
        Intent intent = new Intent(Actividades.contexto, Actividades.actividad);

        //Recorre todos los parámetros que tiene que pasar de serializables
        Iterator iterator = listaSerializable.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();

            String key = (String)pair.getKey();
            Serializable value = (Serializable) pair.getValue();
            intent.putExtra(key, value);
        }

        //Corre la actividad
        Actividades.contexto.startActivity(intent);
    }
}
