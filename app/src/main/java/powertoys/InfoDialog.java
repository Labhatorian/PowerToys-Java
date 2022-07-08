package powertoys;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class InfoDialog extends JDialog {
    private Dimension dim = new Dimension(800, 420);
    private Dimension dimPane = new Dimension(650,300);
    JTextPane pane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(pane);
    private boolean holdUp = false;
    private boolean itWasntMe = false;
    private movePane movePane = new movePane();
    public InfoDialog(JFrame parent) {
        //TODO Get version from somewhere
        setTitle("PowerToys Beta 1.0");
        setSize(dim);
        setPreferredSize(dim);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Tell the thread to bloody close
                movePane.interrupt();
            }
        });

                setLayout(new FlowLayout());

        JLabel title = new JLabel();
        title.setText("Powertoys Beta 1.0");
        title.setFont(new Font(title.getName(), Font.PLAIN, 20));

        JLabel copyright = new JLabel();
        copyright.setText(" \u00A9 Labhatorian 2022 - All Rights Reserved");
        copyright.setFont(new Font(title.getName(), Font.PLAIN, 15));

        pane.setPreferredSize(dimPane);
        pane.setEditable(false);

        //Read the license from Resources
        BufferedReader in = null;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("LICENSE");

            String newLine = System.getProperty("line.separator");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append(newLine);
                }
                result.append(line);
            }
            pane.setText(String.valueOf(result));

        } catch (IOException e) {
        } finally {
            try { in.close(); } catch (Exception ex) { }
        }
        //

        //Align Center
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAttribute, false);
        //
        pane.setCaretPosition(0); // Start at top when opening dialog

        add(title);
        add(scrollPane);
        add(copyright);

        setVisible(true);

        //Prepare moving scrollbar and starts thread
        scrollPane.getViewport().addChangeListener(new ListenAdditionsScrolled());
        movePane.start();
    }
    public class ListenAdditionsScrolled implements ChangeListener{
        public void stateChanged(ChangeEvent e){
            if(!itWasntMe) {
                //Stops the scrollbar automatically moving when the user messes with it
                System.out.println("Konichuwa!");
                holdUp = true;
            }
        }
    }


    //Using thread so this can run on its own without stopping the rest of the program
    //In a while loop so when the user messes with the scrollbar, it stops doing it and waits 10 seconds before restarting.
    private class movePane extends Thread {
            public void run() {
                try {
                    //Get the scrollbar because that's an invidual component for some reason
                    JScrollBar vertical = scrollPane.getVerticalScrollBar();
                    while (!holdUp) {
                        //Move the scrollbar and tell the listener it was not the user.
                        System.out.println(vertical.getValue());
                        itWasntMe = true;
                        vertical.setValue(vertical.getValue() + 1);
                        itWasntMe = false;
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    holdUp = false;
                    movePane = new movePane();
                    movePane.start();
                } catch (Exception e){
                    //do nothing, most likely caused by the window closing
                }
            }
        }
    }