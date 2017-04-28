package com.desenvolvigames.applications.witalk.entities.old;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Joao on 18/04/2017.
 */

public class Tab_Ip extends EntityBase
{
    private Integer pk_int_IdIp;
    private String str_Ip;
    private Date dte_DataAtualizacao;
    private Boolean bit_IpAtivo;
    private ArrayList<Tab_Usuario> Tab_Usuario;

    private ArrayList<Tab_Mensagem_Visualizada_Usuario> Tab_Mensagem_Visualizada_Usuario;
    private ArrayList<Tab_Mensagem> Tab_Mensagem;
    private ArrayList<Tab_Grupo_Usuario_Relacionamento> Tab_Grupo_Usuario_Relacionamento;
    private ArrayList<Tab_Grupo> Tab_Grupo;

    public void setIp(String strIp){str_Ip = strIp;}
    public String getIp(){return str_Ip;}

    public Integer getPkIP(){return this.pk_int_IdIp;}
}
