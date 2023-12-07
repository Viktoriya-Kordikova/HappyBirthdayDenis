import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class FileOpener {

    public static void openFile(String filePath) {
        File file = new File(filePath);

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    desktop.open(file);
                    System.out.println("Файл успешно открыт.");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Ошибка при открытии файла.");
                }
            } else {
                System.out.println("Открытие файлов не поддерживается.");
            }
        } else {
            System.out.println("Рабочий стол не поддерживается.");
        }
    }
}
