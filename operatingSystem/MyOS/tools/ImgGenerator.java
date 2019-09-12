import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 生成软盘镜像的程序
 */
public class ImgGenerator {
    // 启动扇区固定512字节
    private static final int BOOTER_SECTOR_LENGTH = 512;
    // 软盘固定大小
    private static final int SOFT_DISK__LENGTH = 0x00168000;

    public static void main(String[] args) {
        try {
            File imgFile = new File("../output/softDisk.img");
            if (!imgFile.exists()) {
                imgFile.createNewFile();
            }
            File booterFile = new File("../boot/booter.o");
            OutputStream imgFileOutput = new FileOutputStream(imgFile);
            InputStream booterFileInput = new FileInputStream(booterFile);

            byte[] booterSector = new byte[BOOTER_SECTOR_LENGTH];
            booterFileInput.read(booterSector);
            imgFileOutput.write(booterSector);
            byte[] fillZeroBytes = new byte[SOFT_DISK__LENGTH - BOOTER_SECTOR_LENGTH];
            for (int i = 0; i < fillZeroBytes.length; i++) {
                fillZeroBytes[i] = 0;
            }

            imgFileOutput.write(fillZeroBytes);
            booterFileInput.close();
            imgFileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}