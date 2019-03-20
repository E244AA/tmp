package com.server;

import com.client.Logic;
import com.client.ViewFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static int PORT = 8888;
    private static ServerSocket SERVER_SOCKET;
    private static ExecutorService executeIt;
    private static int ind;

    public static ArrayList<byte[]> clients_img;


    public void setPORT(int PORT) {
        Server.PORT = PORT;
    }

    public static void startServer() throws java.io.IOException{
        SERVER_SOCKET = new ServerSocket(PORT);

        while(!SERVER_SOCKET.isClosed()){
            Socket client = SERVER_SOCKET.accept();
            clients_img.add(new byte[0]);
            executeIt.execute(new MultiThread(client, ind));
        }

    }

    public void shutdownServer() throws IOException{
        SERVER_SOCKET.close();
        executeIt.shutdownNow();
    }

    public void restartServer() throws IOException{
        shutdownServer();
        startServer();
    }

    public static void main(String[] args) throws IOException {

        executeIt = Executors.newFixedThreadPool(1000);
        clients_img = new ArrayList<byte[]>(0);

        startServer();

    }

}

class MultiThread implements Runnable{

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;
    private InetAddress ClientInetAddress;
    private Frame frame;

    public MultiThread(Socket client, int ind) throws IOException{

        sock = client;
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
        ClientInetAddress = sock.getInetAddress();
        frame = new Frame();

    }


    public void run() {

        System.out.println(ClientInetAddress + " is connected");

        while(!sock.isClosed()){

            try {

                int len = in.readInt();
                byte[] data = new byte[len];

                for(int i = 0; i < len; i++){
                    data[i] = in.readByte();
                }

                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                BufferedImage b = ImageIO.read(bais);

                System.out.println(b == null);
                frame.setImg(b);

            } catch (IOException e) {
                try {
                    sock.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }

        System.out.println(ClientInetAddress + " is disconnected");

        try {
            in.close();
            out.close();
            sock.close();
        }catch (IOException e){

        }

    }
}

class Frame extends JFrame{

    public static JLabel lbl;

    public Frame(){

        setLayout(new FlowLayout());
        setSize(500,500);

        lbl = new JLabel();
        lbl.setSize(500,500);

        add(lbl);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setImg(BufferedImage img){
        if(img != null) {
            BufferedImage image = flip(resize(img, 500, 500));
            ImageIcon icon = new ImageIcon(image);
            lbl.setIcon(icon);
        }
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
