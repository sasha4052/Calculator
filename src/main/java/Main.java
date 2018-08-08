import javax.swing.*;
import java.io.File;


public class Main {
    public static void main(String[] args) {

        JFileChooser fileopen = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileopen.setCurrentDirectory(workingDirectory);
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            final String fileName = fileopen.getSelectedFile().getPath();
            //final String fileName = ".\\src\\test\\java\\testSum.xml";
            Calculator.calculate(fileName);
        }


    }
}
