package com.example.pocketpenguin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
    private List<Message> messages;
    public List<Message> getMessages() {
        return messages;
    }
    public MessageList(JSONArray array) {
        messages = new ArrayList<>();
        for (int i = array.length(); 0 < i; i--) {
            try {
                JSONObject jsonElement = array.getJSONObject(i-1);
                Message aMessage = new Message(jsonElement);
                messages.add(aMessage);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
