package com.desenvolvigames.applications.witalk.utilities.constants;

/**
 * Created by Joao on 19/04/2017.
 */

public class ConstantsClass
{
    public static String IpExterno;
    public static final String GetIpUrl = "https://api.ipify.org?format=json";

    private static final String PostInserirUrl = "http://192.168.25.2:54492/api/Inserir";
    public static final String PostIpObjectUrl = PostInserirUrl + "/PostIp";
    public static final String PostUsuarioObjectUrl = PostInserirUrl + "/PostUsuario";
}
