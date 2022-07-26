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
    JTable includedFolders;
    DefaultTableModel model = new DefaultTableModel(new Object[] { "Included Folders" }, 0);
    ArrayList<String> data = new ArrayList<>();

    JTable crawledFiles;
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

        add(crawlingButton);
        crawlingButton.addActionListener(this);

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
            crawlingTime();
        }

    }

    private void crawlingTime(){
        dataCF.clear();
        modelCF.setRowCount(0);

        ArrayList<Path> allFiles = new ArrayList<>();
        for (String path:data
        ) {
            try {
                Files.walk(Path.of(path)).filter(Files::isRegularFile).forEach(allFiles::add);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Path path2:allFiles
            ) {
                System.out.println(path2.toString());
                dataCF.add(String.valueOf(path2));
                String[] fileToAdd = {String.valueOf(path2)};
                modelCF.addRow(fileToAdd);
                crawledFiles.setModel(modelCF);
                revalidate();
                repaint();
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
