package com.client;

import com.github.sarxos.webcam.Webcam;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Client {

    private static int PORT = 8888;
    private static String ADDRESS = "localhost";
    private static BufferedImage image;
    private static MyWebCam mwc;
    private static Logic logic;
    private static Webcam webcam;

    public Client() throws IOException {
        logic = new Logic();

        webcam = Webcam.getDefault();
        webcam.open();

    }

    public static void main(String[] args) throws IOException {

        Client client = new Client();
        Socket client_socket = new Socket("127.0.0.1", 8888);

        DataInputStream in = new DataInputStream(client_socket.getInputStream());;
        DataOutputStream out = new DataOutputStream(client_socket.getOutputStream());

        try {

            while(!client_socket.isClosed()) {
                BufferedImage bi = webcam.getImage();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bi, "jpg", baos);
                baos.flush();

                byte[] data = baos.toByteArray();
                baos.flush();

                out.writeInt(data.length);
                for(int i = 0; i < data.length; i++){
                    out.writeByte(data[i]);
                }
            }

            client_socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
