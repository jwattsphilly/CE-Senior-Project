package com.example.ivi.adr;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import java.io.IOException;



import processing.core.PApplet;


/**
 * Created by ivi on 9/10/2015.
 */
public class Sketch extends PApplet {

    public Sketch(Context cin) {
        asset = cin.getAssets();
    }

    MediaPlayer snd;
    AssetManager asset;
    AssetFileDescriptor fd;
    int bitSelector, bytc;
    static int payloadByte;
    boolean isTransmitting, screenTouched;

    @Override
    public void settings() {
        //Call size/fullscreen from here
        size(500, 500);
    }


    @Override
    public void setup() {
        //Initialize one time stuff here
        orientation(LANDSCAPE);
        textAlign(CENTER, CENTER);
        textSize(50);
        bitSelector = 1;
        payloadByte = 0x7D;//125
        setupMediaPlayer();
    }

    public static void parseEditTextInput(String input) {

//        char[] inputChars = input.toCharArray();
//        byte[] payloadBytes = new byte[input.length()];
//
//        for(int i=0; i< inputChars.length; i++)
//            payloadBytes[i] = parseByte(inputChars[i]);
        payloadByte = parseInt(input);
    }

    @Override
    public void draw() {
        background(78, 93, 75);
        text("PUSH " + "\n" + "\n" +
                "TO SEND: " + payloadByte, width / 2, height / 2);
        //payloadByte=parseInt(value);
        sendData();
    }


    private void setupMediaPlayer() {
        snd = new MediaPlayer();
        try {
            fd = asset.openFd("lnoise.wav");
            snd.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            snd.prepare();
            snd.start();
        } catch (IOException e) {
            println("prepare failed");
        }
    }
    private void sendData()
    {
        if (!isTransmitting) {
            snd.setVolume(1,1);
            isTransmitting = true;
        }

        if (mousePressed) {
            screenTouched = true;
        }
        if (bitSelector > 128) {
            bitSelector = 1;
            screenTouched = false;
        }
        snd.setVolume(0, 0);
        delay(10);
        if (screenTouched) {
            bytc = payloadByte & bitSelector; //
            snd.setVolume(1, 1);//data send
            if (bytc == 0) {
                delay(30);//low length
            } else {
                delay(130);////high length
            }
            bitSelector <<= 1;
        }
        //
        snd.setVolume(0, 0);
        delay(10);
        if (!snd.isPlaying()) {
            isTransmitting = false;
        }
    }
}




