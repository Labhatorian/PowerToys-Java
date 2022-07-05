package powertoys;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JPanel implements ActionListener {
    Button fileSorterButton = new Button("File Sorter");
    Button randomFileChooserButton = new Button("Random File Chooser");
    Button fileUnsorterButton = new Button("File Unsorter");

    JFrame app;

        public MainWindow(JFrame app){
            this.app = app;

            setSize(500, 300);
            setLayout(new FlowLayout());

            add(fileSorterButton);
            fileSorterButton.addActionListener(this);
            add(randomFileChooserButton);
            add(fileUnsorterButton);

            setVisible(true);
        }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == fileSorterButton) {
            FileSorter fileSorter = new FileSorter(app);
            app.remove(this);
            app.add(fileSorter);
            app.revalidate();
            app.repaint();
        }
    }
}
