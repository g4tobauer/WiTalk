package com.desenvolvigames.applications.witalk.entities.old;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by Joao on 16/04/2017.
 */

public class Tab_Usuario extends EntityBase
{
    public Tab_Usuario()
    {
        pk_int_IdUsuario = 1;
        fk_int_IdIp = 1;
        str_UsuarioKEY = "1";
        str_NomeUsuario = "1";
        dte_DataAtualizacao = Calendar.getInstance().getTime();
        bit_UsuarioAtivo = true;
    }
    public Integer pk_int_IdUsuario;
    public Integer fk_int_IdIp;
    public String str_UsuarioKEY;
    public String str_NomeUsuario;
    public Date dte_DataAtualizacao;
    public Boolean bit_UsuarioAtivo;
}
