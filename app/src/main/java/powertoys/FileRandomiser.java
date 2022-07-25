package powertoys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileRandomiser extends JPanel implements ActionListener {
    Button returnButton = new Button("Return");
    Button addFolder = new Button("Add folder");
    Button randomiseButton = new Button("Randomise");
    JTable includedFolders;
    String[] columnNames = {"Included folder"};
    DefaultTableModel model = new DefaultTableModel(new Object[] { "Included Folders" }, 0);
    ArrayList<String> data = new ArrayList<>();
    JFrame app;

    public FileRandomiser(JFrame app){
        this.app = app;

        setSize(500, 300);
        setLayout(new FlowLayout());
        add(returnButton);
        returnButton.addActionListener(this);
        app.setTitle("PowerToys - FileRandomiser");
        addFolder.addActionListener(this);
        add(addFolder);
        add(randomiseButton);
        randomiseButton.addActionListener(this);
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
        if (actionEvent.getSource() == randomiseButton) {
            randomiseTime();
        }
    }

    private void randomiseTime(){
        ArrayList<String> files = new ArrayList<>();
        List list = new List();
        Stream<Path> allFiles = null;
        ArrayList<Path> allFiles2 = new ArrayList<>();
        for (String path:data
             ) {
            try {
                Files.walk(Path.of(path)).filter(Files::isRegularFile).forEach(allFiles2::add);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Path ongod:allFiles2
                 ) {
                System.out.println(ongod.toString());
            }
        }
    }
}
