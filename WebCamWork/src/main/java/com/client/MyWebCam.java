package com.client;

import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class MyWebCam {

    public static BufferedImage image;
    private static Webcam webcam;

    public MyWebCam(){
        webcam = Webcam.getDefault();
        webcam.open();
    }

    public ByteBuffer getByteBuffer(){
        return webcam.getImageBytes();
    }

    public BufferedImage getBufferedImage(){

        image = flip(resize(webcam.getImage(), 500, 500));

        return image;
    }

    // Изменение размера изображения
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    // Отражение изображения
    public static BufferedImage flip(BufferedImage img){

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(null), 0);

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        img = op.filter(img, null);

        return img;
    }

}

class GetImg implements Runnable{

    Webcam webc;

    public GetImg(Webcam web){

        webc = web;

    }

    public void run() {

        while(true) {
            MyWebCam.image = MyWebCam.flip(MyWebCam.resize(webc.getImage(), 500, 500));
        }

    }
}