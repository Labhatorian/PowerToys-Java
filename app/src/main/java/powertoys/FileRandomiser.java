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
import java.util.Scanner;

public class FileRandomiser extends JPanel implements ActionListener {
    Button returnButton = new Button("Return");
    Button addFolder = new Button("Add folder");
    Button randomiseButton = new Button("Randomise");
    Button crawlingButton = new Button("Crawl files");
    Button ignoreButton = new Button("Ignore file/folder");
    JRadioButton continousRandomisationButton = new JRadioButton("Continous randomisation");
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

        setSize(850, 300);
        app.setSize(1400, 1000);
        setLayout(new FlowLayout());
        app.setTitle("PowerToys - FileRandomiser");

        add(returnButton);
        returnButton.addActionListener(this);

        add(addFolder);
        addFolder.addActionListener(this);

        add(randomiseButton);
        randomiseButton.addActionListener(this);

        add(continousRandomisationButton);

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
            JFileChooser fc2 = new JFileChooser();
            fc2.setCurrentDirectory(new java.io.File(".")); // start at application current directory
            fc2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = fc2.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File yourFolder = fc2.getSelectedFile();
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

                    //TODO Make function out of adding data
                    if(dataI.size() != 0){
                        for (String ignorePath:dataI
                        ) {
                            if(!add.toString().contains(ignorePath)){
                                dataCF.add(String.valueOf(add));
                                String[] fileToAdd = {String.valueOf(add)};
                                modelCF.addRow(fileToAdd);
                                crawledFiles.setModel(modelCF);
                                revalidate();
                                repaint();
                            }
                        }
                    } else {
                        dataCF.add(String.valueOf(add));
                        String[] fileToAdd = {String.valueOf(add)};
                        modelCF.addRow(fileToAdd);
                        crawledFiles.setModel(modelCF);
                        revalidate();
                        repaint();
                    }

                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void randomiseTime() {
        ArrayList<String> processBefore = new ArrayList<>();
        ArrayList<String> processAfter = new ArrayList<>();
        String processTarget = null;

        findProcesses(processBefore);

        //Now choose a random file to open
        Random rand = new Random();
        String randomElement = dataCF.get(rand.nextInt(dataCF.size()));

        File file = new File(randomElement);
        //first check if Desktop is supported by Platform or not
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(app, e.getMessage(), "Powertoys: Something went wrong opening the file!", JOptionPane.ERROR_MESSAGE);
            }
        }
        findProcesses(processAfter);

        boolean processTargetFound = false;

        for (String processB:processBefore
        ) {
            for (String processA:processAfter
            ) {
                if(processB.equals(processA)){
                    //There is one, nothing wrong
                    processTargetFound = true;
                    break;
                }
            }
            if(!processTargetFound){
                processTarget = processB;
            } else {
                processTargetFound = false;
            }
        }

        String finalProcessTarget = processTarget;
        new Thread(() -> {
        //Find what program is running now after opening
        ArrayList<String> processBefore2 = new ArrayList<>();
        ArrayList<String> processAfter2 = new ArrayList<>();

        if (continousRandomisationButton.isSelected()) {
            findProcesses(processBefore2);
            boolean programOpen = true;

            while (programOpen) {
                processAfter2.clear();
                if (continousRandomisationButton.isSelected()) {
                        findProcesses(processAfter2);
                    //TODO Rewrite to find the string we are looking for and not find the odd one out.

                    for (String processSeek:processBefore2
                         ) {
                        if (processSeek.equals(finalProcessTarget)) {
                            //There is one, nothing wrong
                            programOpen = false;
                            break;
                        }
                    }

                    //Are you still there?
                    synchronized (this){
                        try{
                            this.wait(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            //Still selected? Continue randomising
            if (continousRandomisationButton.isSelected()) {
                randomiseTime();
            }
        }
        }).start();
    }

    private void findProcesses(ArrayList<String> processArray){
        Process process;
        try {
            process = new ProcessBuilder("powershell", "\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders  name, ID").start();
            Scanner sc = new Scanner(process.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                processArray.add(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(app, e.getMessage(), "Powertoys: Something went wrong!", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Done");
    }
}
