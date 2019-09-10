import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 生成软盘镜像的程序
 */
public class ImgGenerator {
    public static void main(String[] args) {
        try {
            File imgFile = new File("softDisk.img");
            if (!imgFile.exists()) {
                imgFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(imgFile);
            byte[] startFixedBytes = new byte[] { (byte) 0xEB, (byte) 0x4E, (byte) 0x90, (byte) 0x48, (byte) 0x45 };
            outputStream.write(startFixedBytes);
            byte[] fillZeroBytes = new byte[0x000001FE - startFixedBytes.length];
            for(int i =0; i<fillZeroBytes.length;i++){
                fillZeroBytes[i] = 0;
            }
            outputStream.write(fillZeroBytes);
            byte[] fixed01FEBytes = new byte[]{(byte)0x55, (byte)0xAA, (byte)0xF0, (byte)0xFF, (byte)0xFF};
            outputStream.write(fixed01FEBytes);
            fillZeroBytes = new byte[0x00001400 - 0x00000202 -1];
            for(int i =0; i<fillZeroBytes.length;i++){
                fillZeroBytes[i] = 0;
            }
            outputStream.write(fillZeroBytes);
            byte[] fixed1400Bytes = new byte[]{(byte)0xF0, (byte)0xFF, (byte)0xFF};
            outputStream.write(fixed1400Bytes);
            fillZeroBytes = new byte[0x00168000 - 0x00001402 - 1];
            for(int i =0; i<fillZeroBytes.length;i++){
                fillZeroBytes[i] = 0;
            }
            outputStream.write(fillZeroBytes);
            outputStream.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}