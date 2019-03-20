package com.client;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ViewFrame {

    public static JLabel lbl;
    public static BufferedImage bi;

    public ViewFrame(){

        bi = null;

        /*
        Создаю окно, помещаю лэйбл, в котором буду отображать изображение
        */

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(500,500);

        lbl = new JLabel();
        lbl.setSize(500,500);

        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
        while (true){
            if(bi != null) {
                lbl.setIcon(new ImageIcon(bi));
            }
        }
        */


        Thread thread = new Thread(new UpdateLabel(this));
        thread.start();

    }

    public void setImg(BufferedImage image){
        this.bi = image;
    }

}

class UpdateLabel implements Runnable{

    ViewFrame v;

    public UpdateLabel(ViewFrame vf){
        v = vf;
    }

    public void run() {

        while(true) {

            if(ViewFrame.bi != null) {
                v.lbl.setIcon(new ImageIcon(v.bi));
            }
        }

    }
}
