package com.desenvolvigames.applications.witalk.entities;


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
    protected Integer pk_int_IdUsuario;
    protected Integer fk_int_IdIp;
    protected String str_UsuarioKEY;
    protected String str_NomeUsuario;
    protected Date dte_DataAtualizacao;
    protected Boolean bit_UsuarioAtivo;
}
