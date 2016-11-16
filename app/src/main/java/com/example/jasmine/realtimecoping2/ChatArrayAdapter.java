package com.example.jasmine.realtimecoping2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmine on 4/20/16.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage>{
    private TextView chatText;
    private ImageView chatImage;
    private VideoView chatVideo;
    private List<ChatMessage> MessageList = new ArrayList<ChatMessage>();
    private LinearLayout layout;
    private Context context = getContext();

    public ChatArrayAdapter(Context context,int textViewResourseId){
        super(context, textViewResourseId);
    }

    public void add(ChatMessage object){
        MessageList.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.MessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.MessageList.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        View v = ConvertView;
        if (v==null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat, parent, false);
        }

        layout = (LinearLayout)v.findViewById(R.id.Message1);
        ChatMessage messageobj = getItem(position);
        if (messageobj.type.equals("text")){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat, parent, false);
            chatText = (TextView)v.findViewById(R.id.GreetingMessage);
            chatText.setText((String)messageobj.message);
            chatText.setBackgroundResource(messageobj.left ? R.drawable.bubbleb : R.drawable.bubblea);
            layout.setGravity(messageobj.left? Gravity.LEFT:Gravity.RIGHT);
        }
        if (messageobj.type.equals("image")){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.image, parent, false);
            chatImage = (ImageView)v.findViewById(R.id.funnypics);
            chatImage.setImageResource((Integer) messageobj.message);
            layout.setGravity(messageobj.left? Gravity.LEFT:Gravity.RIGHT);
        }
        /*if (messageobj.type.equals("video")){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
            v = inflater.inflate(R.layout.videos, parent, false);
            chatVideo = (VideoView)v.findViewById(R.id.funnyvideos);
            String path= (String)messageobj.message;
            Uri uri=Uri.parse(path);
            VideoView video=(VideoView)v.findViewById(R.id.funnyvideos);
            video.setMediaController(new MediaController(context));
            video.setVideoURI(uri);
            video.start();
            video.requestFocus();
        }**/
        return v;

    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
