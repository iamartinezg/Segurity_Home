package com.example.proyecto.modelos;

public class DatabaseRoutes {
    public final static String USERS_PATH = "users";
    public final static String MESSAGES_PATH = "messages";
    public final static String CASAS_PATH = "casas";

    public static String getUser (String uuid){
        return String.format("%s/%s", USERS_PATH, uuid);
    }

    public static String getMessage (String uuid){return String.format("%s/%s", MESSAGES_PATH, uuid);}

    public static String getCasa (String uuid){return String.format("%s/%s", CASAS_PATH, uuid);}
}

