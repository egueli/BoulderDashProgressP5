package com.e_gueli.boulderprogress;

import processing.core.PApplet;
import processing.serial.Serial;

public class ArduinoBitmapSender {
    private Serial arduinoPort;
    private String lastBitmapSent;

    void setup(BoulderDashProgress pApplet) {
        PApplet.println(Serial.list());
        String portName = Serial.list()[0];
        arduinoPort = new Serial(pApplet, portName, 115200);
    }

    void sendFieldToArduino(BoulderFieldState fieldState) {
        boolean[][] bitmap = fieldState.toArrayOfSettled();
        byte[] columnBytes = new byte[fieldState.getHeight()];
        for (int x = 0; x < fieldState.getWidth(); x++) {
            byte columnByte = 0;
            for (int y = 0; y < fieldState.getHeight(); y++) {
                columnByte |= (bitmap[x][y] ? 1 : 0) << y;
            }
            columnBytes[x] = columnByte;
        }

        String bitmapString = bytesToHex(columnBytes);
        if (!bitmapString.equals(lastBitmapSent)) {
            arduinoPort.write(String.format("m%s\n", bitmapString));
            lastBitmapSent = bitmapString;
        }
    }

    private final char[] hexArray = "0123456789ABCDEF".toLowerCase().toCharArray();

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
