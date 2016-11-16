package com.example.jasmine.realtimecoping2;

import android.content.Context;

/**
 * Created by Jasmine on 4/20/16.
 */
public class ChatMessage {
    final boolean left;
    Object message;
    String type;
    //String answer;

    public ChatMessage(boolean left, Object message, String type){
        super();
        this.left=left;
        this.type = type;
        this.message = message;
        //this.answer = answer;
    }

}
