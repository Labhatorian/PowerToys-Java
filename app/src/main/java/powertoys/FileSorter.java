package powertoys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileSorter extends JPanel implements ActionListener {
    Button returnButton = new Button("Return");
    JFrame app;

    public FileSorter(JFrame app){
        this.app = app;

        setSize(500, 300);
        setLayout(new FlowLayout());
        add(returnButton);
        returnButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == returnButton) {
            MainWindow mainWindow = new MainWindow(app);
            app.remove(this);
            app.add(mainWindow);
            app.revalidate();
            app.repaint();
        }
    }
}
