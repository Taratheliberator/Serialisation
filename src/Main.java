import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        List<String> filePaths = new ArrayList<>();
        GameProgress save1 = new GameProgress(80, 3, 21, 150.0);
        String pathSave1 = "C://Games//savegames//save1.dat";
        if (saveGame(pathSave1, save1)) filePaths.add(pathSave1);
        GameProgress save2 = new GameProgress(70, 2, 19, 106.0);
        String pathSave2 = "C://Games//savegames//save2.dat";
        if (saveGame(pathSave2, save2)) filePaths.add(pathSave2);
        GameProgress save3 = new GameProgress(100, 5, 31, 88.0);
        String pathSave3 = "C://Games//savegames//save3.dat";
        if (saveGame(pathSave3, save3)) filePaths.add(pathSave3);

        zipFiles("C://Games//savegames//zip.zip", filePaths);

    }

    public static boolean saveGame(String pathFile, GameProgress save) {
        File saveFile = new File(pathFile);
        try {
            if (!saveFile.createNewFile()) {
                System.out.println("Игра не сохранена, потому что не создан файл");
                return false;
            } else {
                try (FileOutputStream fos = new FileOutputStream(saveFile.getAbsolutePath());
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(save);
                    System.out.println("Игра сохранена в " + pathFile);
                } catch (IOException ex1) {
                    System.out.println(ex1.getMessage());
                }
            }
        } catch (IOException ex2) {
            System.out.println(ex2.getMessage());
        }
        return true;
    }

    public static void zipFiles(String pathArchive, List<String> filePaths) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathArchive))) {
            for (int i = 0; i < filePaths.size(); i++) {
                try (FileInputStream fis = new FileInputStream(filePaths.get(i))) {
                    ZipEntry entry = new ZipEntry("packed_save" + (i + 1) + ".dat");
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        File file = new File(pathArchive);
        if (file.exists()) {
            StringBuilder string = new StringBuilder();
            string.append("Архив создан по адресу: ")
                    .append(pathArchive)
                    .append("\n");
            for (String filePath : filePaths) {
                File save = new File(filePath);
                if (save.delete()) string.append("Файл ")
                        .append(filePath)
                        .append(" удален\n");
            }
            filePaths.clear();
            System.out.println(string);
        } else System.out.println("Архив не создан");
    }

}