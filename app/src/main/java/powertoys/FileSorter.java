package powertoys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class FileSorter extends JPanel implements ActionListener {
    Button returnButton = new Button("Return");
    Button addFolder = new Button("Add folder");
    JTable includedFolders;
    String[] columnNames = {"Included folder"};
    DefaultTableModel model = new DefaultTableModel(new Object[] { "Included Folders" }, 0);
    ArrayList<String> data = new ArrayList<>();
    JFrame app;

    public FileSorter(JFrame app){
        this.app = app;

        setSize(500, 300);
        setLayout(new FlowLayout());
        add(returnButton);
        returnButton.addActionListener(this);
        app.setTitle("PowerToys - FileSorter");
        addFolder.addActionListener(this);
        add(addFolder);


        includedFolders = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(includedFolders);
        includedFolders.setFillsViewportHeight(true);
        add(scrollPane);
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
        if (actionEvent.getSource() == addFolder) {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File yourFolder = fc.getSelectedFile();
                data.add(yourFolder.getPath());
                model.addRow(data.toArray());
                includedFolders.setModel(model);
                revalidate();
                repaint();
            }
        }
    }
}
