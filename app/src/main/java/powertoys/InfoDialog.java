package powertoys;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;

public class InfoDialog extends JDialog {

    public InfoDialog(){
        setTitle("PowerToys Beta 0.1");
        setSize(800, 500);
        setLayout(new FlowLayout());

        JTextPane pane = new JTextPane();

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

        add(pane);
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAttribute, false);
        setVisible(true);
    }
}
