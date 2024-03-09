package com.example.pocketpenguin;
import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    private int id;
    private int family_pk;
    private String date;
    private String user;
    private String content;
    public String getContent(){
        return content;
    }
    public String getUser() {
        return user;
    }
    public String getDate() {
        return date;
    }
    public Message(JSONObject json) throws JSONException {
        this.id = json.getInt("chat_id");
        this.user = json.getString("user");
        this.date=json.getString("date");
        this.content = json.getString("message");
        this.family_pk = json.getInt("family");
    }
}
