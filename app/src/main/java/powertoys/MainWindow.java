package powertoys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JPanel implements ActionListener {
    Button fileSorterButton = new Button("File Sorter");
    Button randomFileChooserButton = new Button("File Randomiser");
    Button fileUnsorterButton = new Button("File Unsorter");

    JFrame app;

        public MainWindow(JFrame app){
            this.app = app;

            setSize(500, 300);
            setLayout(new FlowLayout());

            add(fileSorterButton);
            fileSorterButton.addActionListener(this);
            add(randomFileChooserButton);
            randomFileChooserButton.addActionListener(this);
            add(fileUnsorterButton);
            fileUnsorterButton.addActionListener(this);
            app.setTitle("PowerToys");

            setVisible(true);
        }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == randomFileChooserButton) {
            FileRandomiser fileRandomiser = new FileRandomiser(app);
            movePanels(fileRandomiser);
        }

        if (actionEvent.getSource() == fileSorterButton || actionEvent.getSource() == fileUnsorterButton) {
            JOptionPane.showMessageDialog(app, "Currently not implemented", "Powertoys: Hold up!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void movePanels(Object object){
        app.remove(this);
        app.add((Component) object);
        app.revalidate();
        app.repaint();
    }
}
