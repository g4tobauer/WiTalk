package com.desenvolvigames.applications.witalk.entities;

/**
 * Created by NOTEBOOK on 06/07/2017.
 */

public class MessageBody{
    private final String collapse_key = "score_update";
    private final int time_to_live = 108;
    private final boolean delay_while_idle = true;
    public String to;
    public Notification notification;
    public Data data;

    public MessageBody(){
        data = new Data();
        notification = new Notification();
    }
    public class Notification{
        public String body;
        public String title;
        public String icon;
        private Notification(){}
    }
    public class Data{
        public String UserId;
        public String UserMessageToken;
        public String UserName;
        public String UserMessage;
        private Data(){}
    }
}
