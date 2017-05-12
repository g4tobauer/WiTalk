package com.desenvolvigames.applications.witalk.utilities.constants;

import com.desenvolvigames.applications.witalk.entities.Ip;
import com.desenvolvigames.applications.witalk.entities.Usuario;

/**
 * Created by Joao on 19/04/2017.
 */

public class ConstantsClass
{
    public static String IpExterno;
    public static Usuario Usuario;
    public static final String GetIpUrl = "https://api.ipify.org?format=json";

    private static final String PostInserirUrl = "http://192.168.25.2:54492/api/Inserir";
    public static final String PostIpObjectUrl = PostInserirUrl + "/PostIp";
    public static final String PostUsuarioObjectUrl = PostInserirUrl + "/PostUsuario";
}
