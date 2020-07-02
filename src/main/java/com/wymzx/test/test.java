package com.wymzx.test;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class test {
    public static void main(String[] args) {
        //枚举类型，获取所有的通行端口，包括232（PORT_SERIAL）、485、并口等等
        Enumeration enumeration= CommPortIdentifier.getPortIdentifiers();
        while (enumeration.hasMoreElements()){
            //判断enumeration里面是否有更多的元素
            //获取下一个元素，该元素包含某个通信端口的所有信息
            CommPortIdentifier commPortIdentifier=
                    (CommPortIdentifier) enumeration.nextElement();
            //如果该端口的类型是串口
            if (commPortIdentifier.getPortType()==CommPortIdentifier.PORT_SERIAL) {
                //判断该串口的名称
                if (commPortIdentifier.getName().equals("COM3")) {
                    try {
                        //打开串口，获得该串口的serialPort对象
                        SerialPort serialPort =
                                (SerialPort) commPortIdentifier.open("", 2000);
                        //设置该串口参数，9600,8,1,n
                        serialPort.setSerialPortParams(9600, 8, 1, 0);
                        //获取输出流，利用输出流发送数据
                        String a="FFFF0B7601020160C8FFF7";
                        OutputStream outputStream = serialPort.getOutputStream();
                        outputStream.write(toByteArray(a));

                        //一定要关闭串口，否则会阻塞该串口，直到你关闭程序
                        serialPort.close();
                        outputStream.close();

                    } catch (PortInUseException e) {
                        System.out.println("PortInUseException抛出，串口被使用");
                        e.printStackTrace();
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println("UnsupportedCommOperationException抛出");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("IOException抛出");
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }
}
