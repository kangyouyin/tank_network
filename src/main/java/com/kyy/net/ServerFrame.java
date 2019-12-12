package com.kyy.net;;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by kangyouyin on 2019/12/11.
 */
public class ServerFrame extends Frame {
    public static final ServerFrame INSTANCE = new ServerFrame();

    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();
    Server server = new Server();

    private ServerFrame() {
        this.setSize(1600, 600);
        this.setLocation(300, 30);
        this.add(btnStart, BorderLayout.NORTH);
        Panel p = new Panel(new GridLayout(1, 2));
        p.add(taLeft);
        p.add(taRight);
        this.add(p);

        taLeft.setFont(new Font("verderna", Font.PLAIN, 25));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.start();
    }


    public void updateServerMsg(String msg) {
        this.taLeft.setText(taLeft.getText() + msg + System.getProperty("line.separator"));
    }

    public void updateClientMsg(String msg) {
        this.taRight.setText(taRight.getText() + msg + System.getProperty("line.separator"));
    }
}
