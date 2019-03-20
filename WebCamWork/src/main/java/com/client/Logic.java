package com.client;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Logic {

    private static ArrayList<ViewFrame> frames;
    private static int cnt_clients;
    private static int tmp;

    public Logic(){
        frames = new ArrayList<ViewFrame>(0);
        frames.add(new ViewFrame());
        cnt_clients = 0;
        tmp = 0;
    }

    public void setCountOfClients(int cnt){
        cnt_clients = cnt;
        if(frames.size() < cnt){
            for(int i = frames.size(); i < cnt; i++){
                frames.add(new ViewFrame());
            }
        }
        else if(frames.size() > cnt){
            for(int i = frames.size() - 1; i >= cnt; i--){
                frames.remove(frames.get(i));
            }
        }
    }

    public void showClient(BufferedImage img){

        frames.get(tmp).setImg(img);
        tmp++;

        if(tmp == cnt_clients) tmp = 0;

    }

    public void showClientForServer(BufferedImage img){

        ViewFrame vf = new ViewFrame();
        vf.setImg(img);

    }

}
