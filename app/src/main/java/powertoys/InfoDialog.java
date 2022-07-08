package powertoys;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;

public class InfoDialog extends JDialog {
    private Dimension dim = new Dimension(800, 420);
    private Dimension dimPane = new Dimension(650,300);
    public InfoDialog(){
        //TODO Get version from somewhere
        setTitle("PowerToys Beta 1.0");
        setSize(dim);
        setPreferredSize(dim);
        setLayout(new FlowLayout());

        JLabel title = new JLabel();
        title.setText("Powertoys Beta 1.0");
        title.setFont(new Font(title.getName(), Font.PLAIN, 20));

        JLabel copyright = new JLabel();
        copyright.setText(" \u00A9 Labhatorian 2022");
        copyright.setFont(new Font(title.getName(), Font.PLAIN, 15));

        JTextPane pane = new JTextPane();
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

        JScrollPane scrollPane = new JScrollPane(pane);
        pane.setCaretPosition(0); // Start at top when opening dialog

        add(title);
        add(scrollPane);
        add(copyright);

        setVisible(true);
    }

    private void movePane(){
        //TODO Move pane after short amount of no scrollpane movement downwards. Use timers
    }
}
