package com.kyy.net;

import com.kyy.Dir;
import com.kyy.Tank;
import com.kyy.TankFrame;

import java.io.*;
import java.util.UUID;


/**
 * Created by kangyouyin on 2019/12/11.
 */
public class TankStopMsg extends Msg {
    public int x, y;
    public Dir dir;
    public UUID id;

    public TankStopMsg(Tank t) {
        this.x = t.getX();
        this.y = t.getY();
        this.dir = t.getDir();
        this.id = t.getId();
    }

    public TankStopMsg(int x, int y, Dir dir, UUID id) {
        super();
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.id = id;
    }

    public TankStopMsg() {
    }

    @Override
    public void handle() {
        if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId())) {
            return;
        }
        Tank tank = TankFrame.INSTANCE.findTankByUUID(this.id);
        if (tank != null) {
            tank.setMoving(false);
            tank.setX(this.x);
            tank.setY(this.y);
            tank.setDir(this.dir);
        }
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.flush();

            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(), dis.readLong());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankStop;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName())
                .append("[")
                .append("uuid=" + id + " | ")
                .append("x=" + x + " | ")
                .append("y=" + y + " | ")
                .append("dir=" + dir + " | ")
                .append("]");
        return builder.toString();
    }
}
