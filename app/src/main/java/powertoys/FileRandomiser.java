package powertoys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class FileRandomiser extends JPanel implements ActionListener {
    Button returnButton = new Button("Return");
    Button addFolder = new Button("Add folder");
    Button randomiseButton = new Button("Randomise");
    JTable includedFolders;
    String[] columnNames = {"Included folder"};
    DefaultTableModel model = new DefaultTableModel(new Object[] { "Included Folders" }, 0);
    ArrayList<String> data = new ArrayList<>();

    JTable crawledFiles;
    String[] columnNamesCF = {"Crawled files"};
    DefaultTableModel modelCF = new DefaultTableModel(new Object[] { "Crawled files" }, 0);
    ArrayList<String> dataCF = new ArrayList<>();

    JFrame app;

    public FileRandomiser(JFrame app){
        this.app = app;

        setSize(800, 300);
        app.setSize(1200, 500);
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

        crawledFiles = new JTable(modelCF);
        JScrollPane scrollPane2 = new JScrollPane(crawledFiles );
        crawledFiles .setFillsViewportHeight(true);
        add(scrollPane2);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == returnButton) {
            MainWindow mainWindow = new MainWindow(app);
            app.setSize(500, 300); // return size back
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

    //TODO rename variables
    //TODO move crawler to its own function
    private void randomiseTime(){
        ArrayList<String> dataCF = new ArrayList<>();
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
                dataCF.add(String.valueOf(ongod));
                String[] fileToAdd = {String.valueOf(ongod)};
                modelCF.addRow(fileToAdd);
                crawledFiles.setModel(modelCF);
                revalidate();
                repaint();
            }
        }

        //Now choose a random file to open
        Random rand = new Random();
        String randomElement = dataCF.get(rand.nextInt(dataCF.size()));

        File file = new File(randomElement);
        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(app, e.getMessage(), "Powertoys: Something went wrong opening the file!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
