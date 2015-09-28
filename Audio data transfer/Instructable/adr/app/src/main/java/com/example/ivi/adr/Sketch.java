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
    int aaa, bytc;
    static int byt;
    boolean a, aa;

    @Override
    public void settings() {
        //Call size/fullscreen from here
        size(500, 500);


    }


    @Override
    public void setup() {
        //Initialize one time stuffs here
        orientation(LANDSCAPE);
        textAlign(CENTER, CENTER);
        textSize(50);
        aaa = 1;
        byt = 0x7D;//125
        //dec 125 first bit start from byt[1]=1;

    }

    public static void karli(String aba) {

        byt = parseInt(aba);
    }

    @Override
    public void draw() {
        background(78, 93, 75);


        text("PUSH " + "\n" + "\n" +
                "TO SEND: " + byt, width / 2, height / 2);
        //byt=parseInt(value);
        if (!a) {
            stp();
            a = true;
        }
        //data send block

        if (mousePressed) {
            aa = true;
        }
        if (aaa > 128) {
            aaa = 1;
            aa = false;
        }
        snd.setVolume(0, 0);
        delay(10);
        if (aa) {
            bytc = byt & aaa; //
            snd.setVolume(1, 1);//data send
            if (bytc == 0) {
                delay(30);//low length
            } else {
                delay(130);////high length
            }
            aaa <<= 1;
        }
        //
        snd.setVolume(0, 0);
        delay(10);
        if (!snd.isPlaying()) {
            a = false;
        }
    }


    void stp() {
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
}




