package com.gcodedevelopers.kenyanrides;

public class chatsData {

    private String chat_with;
    private String chat_with_phone_number;


    public  chatsData(){

    }

    public chatsData(String chat_with, String chat_with_phone_number) {
        this.chat_with = chat_with;
        this.chat_with_phone_number = chat_with_phone_number;

    }

    public String getChat_with() {
        return chat_with;
    }

    public void setChat_with(String chat_with) {
        this.chat_with = chat_with;
    }

    public String getChat_with_phone_number() {
        return chat_with_phone_number;
    }

    public void setChat_with_phone_number(String chat_with_phone_number) {
        this.chat_with_phone_number = chat_with_phone_number;
    }
}
