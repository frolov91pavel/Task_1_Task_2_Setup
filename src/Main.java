import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        ////+++Задача №1 Установка
        createDirectory("", "Games");
        createDirectory("Games", "src");
        createDirectory("Games", "res");
        createDirectory("Games", "savegames");
        createDirectory("Games", "temp");

        createDirectory("Games/src", "main");
        createDirectory("Games/src", "test");

        createFile("Main", "java", "Games/src/main");
        createFile("Utils", "java", "Games/src/main");

        createDirectory("Games/res", "drawables");
        createDirectory("Games/res", "vectors");
        createDirectory("Games/res", "icons");

        createFile("temp", "txt", "Games/temp/");
        ////---Задача №1 Установка

        ////+++Задача №2 Сохранение
        GameProgress gameProgress1 = new GameProgress(100, 1, 1, 10.5);
        GameProgress gameProgress2 = new GameProgress(95, 1, 1, 10.5);
        GameProgress gameProgress3 = new GameProgress(90, 1, 1, 10.5);

        saveGame("Games/savegames", "save1", "dat", gameProgress1);
        saveGame("Games/savegames", "save2", "dat", gameProgress2);
        saveGame("Games/savegames", "save3", "dat", gameProgress3);

        ArrayList<String> filePath = new ArrayList<String>();
        filePath.add("Games/savegames/save1.dat");
        filePath.add("Games/savegames/save2.dat");
        filePath.add("Games/savegames/save3.dat");

        zipFiles("Games/savegames", "zip.zip", filePath);

        FilesDelete("Games/savegames/zip.zip","Games/savegames/");
        ////---Задача №2 Сохранение
    }

    public static void createDirectory(String parent, String current) {

        File dir;

        if (current.equals("")) {
            System.out.println("В команду не передано наименование каталога!");
            return;
        }

        if (parent.equals("")) {
            dir = new File(current);
        } else {
            dir = new File(parent + "/" + current);
        }

        if (dir.mkdir()) {
            System.out.println("Каталог " + dir.getName() + " создан!");
        } else {
            System.out.println("Каталог " + dir.getName() + "  не удалось создать или он существует!");
        }

    }

    public static void createFile(String fileName, String fileType, String filePath) {

        File file;

        if (!fileName.equals("") && !fileName.equals("") && !fileType.equals("")) {
            file = new File(filePath + "//" + fileName + "." + fileType);
            try {
                if (file.createNewFile()) {
                    System.out.println("Файл " + fileName + "." + fileType + " был создан!");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("В команду не передан один из параметров!");
        }

    }

    public static void saveGame(String filePath, String fileName, String fileType, GameProgress gameProgress) {

        try (FileOutputStream fos = new FileOutputStream(filePath + "/" + fileName + "." + fileType); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void zipFiles(String zipPath, String zipName, List<String> filePath) {

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath + "/" + zipName))) {

            for (String name : filePath) {
                FileInputStream fis = new FileInputStream(name);

                String[] listName = name.split("/");
                String packedName = listName[listName.length - 1];

                ZipEntry entry = new ZipEntry(packedName);
                zout.putNextEntry(entry);

                byte[] buffer = new byte[fis.available()];

                fis.read(buffer);
                zout.write(buffer);

                fis.close();
                zout.closeEntry();
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    public static void FilesDelete(String pathZipFile, String pathFileDelete) {

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathZipFile))) {

            ZipEntry entry;
            String nameZipFile;
            String nameFile;

            while ((entry = zin.getNextEntry()) != null) {

                nameZipFile = entry.getName();
                zin.closeEntry();

                File dir = new File(pathFileDelete);

                for (File item : dir.listFiles()) {
                    nameFile = item.getName();

                    if (nameFile.equals(nameZipFile)){
                        item.delete();
                    }
                }

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
