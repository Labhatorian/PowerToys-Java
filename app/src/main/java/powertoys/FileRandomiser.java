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
import java.util.Random;

public class FileRandomiser extends JPanel implements ActionListener {
    Button returnButton = new Button("Return");
    Button addFolder = new Button("Add folder");
    Button randomiseButton = new Button("Randomise");
    Button crawlingButton = new Button("Crawl files");
    Button ignoreButton = new Button("Ignore file/folder");
    JTable includedFolders;
    DefaultTableModel model = new DefaultTableModel(new Object[] { "Included Folders" }, 0);
    ArrayList<String> data = new ArrayList<>();

    JTable crawledFiles;
    DefaultTableModel modelCF = new DefaultTableModel(new Object[] { "Crawled files" }, 0);
    ArrayList<String> dataCF = new ArrayList<>();

    JTable ignoredFilesFolders;
    DefaultTableModel modelI = new DefaultTableModel(new Object[] { "Ignored" }, 0);
    ArrayList<String> dataI = new ArrayList<>();

    JFrame app;

    public FileRandomiser(JFrame app){
        this.app = app;

        setSize(800, 300);
        app.setSize(1400, 1000);
        setLayout(new FlowLayout());
        app.setTitle("PowerToys - FileRandomiser");

        add(returnButton);
        returnButton.addActionListener(this);

        add(addFolder);
        addFolder.addActionListener(this);

        add(randomiseButton);
        randomiseButton.addActionListener(this);

        add(crawlingButton);
        crawlingButton.addActionListener(this);

        add(ignoreButton);
        ignoreButton.addActionListener(this);

        includedFolders = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(includedFolders);
        includedFolders.setFillsViewportHeight(true);
        add(scrollPane);

        crawledFiles = new JTable(modelCF);
        JScrollPane scrollPane2 = new JScrollPane(crawledFiles);
        crawledFiles .setFillsViewportHeight(true);
        add(scrollPane2);

        ignoredFilesFolders = new JTable(modelI);
        JScrollPane scrollPane3 = new JScrollPane(ignoredFilesFolders);
        ignoredFilesFolders .setFillsViewportHeight(true);
        add(scrollPane3);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == returnButton) {
            MainWindow mainWindow = new MainWindow(app);
            app.setSize(520, 300); // return size back
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
        if (actionEvent.getSource() == crawlingButton) {

            Runnable myrunnable = new Runnable() {
                public void run() {
                    crawlingTime();
                }
            };

            new Thread(myrunnable).start();
        }

        if (actionEvent.getSource() == ignoreButton) {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File yourFolder = fc.getSelectedFile();
                dataI.add(yourFolder.getPath());
                modelI.addRow(dataI.toArray());
                ignoredFilesFolders.setModel(modelI);
                revalidate();
                repaint();
            }
        }
    }

    private void crawlingTime(){
        dataCF.clear();
        modelCF.setRowCount(0);
        
        for (String path:data
        ) {
            try {
                Files.walk(Path.of(path)).filter(Files::isRegularFile).forEach(add ->{
                    System.out.println(add.toString());
                    dataCF.add(String.valueOf(add));
                    String[] fileToAdd = {String.valueOf(add)};
                    modelCF.addRow(fileToAdd);
                    crawledFiles.setModel(modelCF);
                    revalidate();
                    repaint();
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void randomiseTime(){
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
