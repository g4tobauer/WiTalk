package com.desenvolvigames.applications.witalk.utilities.constants;

import com.desenvolvigames.applications.witalk.entities.Usuario;

/**
 * Created by Joao on 19/04/2017.
 */

public class ConstantsClass
{
    public static String IpExterno;
    public static Usuario Usuario;
    public static final String GetIpUrl = "https://api.ipify.org?format=json";
    public static final String PostFirebaseMessage = "https://fcm.googleapis.com/fcm/send";

    private static final String PostInserirUrl = "http://192.168.25.2:54492/api/Inserir";
    public static final String PostIpObjectUrl = PostInserirUrl + "/PostIp";
    public static final String PostUsuarioObjectUrl = PostInserirUrl + "/PostUsuario";

    public static final String ProviderFirebaseId = "firebase";
    public static final String ProviderFacebookId = "facebook.com";

    public static final String IpUsuario = "IpUsuario";
    public static final String UserMessageToken = "UserMessageToken";
    public static final String FirebaseUid = "FirebaseUid";
    public static final String FacebookUid = "FacebookUid";
    public static final String Nome = "Nome";
    public static final String Email = "Email";
    public static final String ImgUrl = "ImgUrl";
    public static final String SyncTime = "SyncTime";
}
