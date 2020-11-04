import org.apache.commons.lang3.StringEscapeUtils;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {

    public boolean keepScrapin = false;

    private ArrayList<String> data3080 = new ArrayList<String>();
    private ArrayList<String> data3090 = new ArrayList<String>();

    private UrlTextPane pane3080 = new UrlTextPane();
    private UrlTextPane pane3090 = new UrlTextPane();

    private JTextField url3080 = new JTextField("https://www.newegg.com/p/pl?N=100007709%20601357247");
    private JTextField url3090 = new JTextField("https://www.newegg.com/p/pl?N=100007709%20601357248");

    public Scraper() {

        JPanel panel3080 = new JPanel();
        JPanel panel3090 = new JPanel();

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pane3080.setText(convertText(data3080));
                pane3090.setText(convertText(data3090));
            }
        });
        timer.start();

        JButton enableScrape = new JButton("Show In Stock");
        enableScrape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                keepScrapin = true;
                setUrls(url3080.getText(), url3090.getText());
            }
        });

        JButton disableScrape = new JButton("Show Out Of Stock");
        disableScrape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                keepScrapin = false;
                setUrls(url3080.getText(), url3090.getText());
            }
        });

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new GridLayout(1, 2));
        mainContainer.add(setup3080Panel(panel3080, pane3080));
        mainContainer.add(setup3090Panel(panel3090, pane3090));
        mainContainer.setPreferredSize(new Dimension(1000, 660));
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout(2, 2));
        buttonContainer.add(url3080);
        buttonContainer.add(url3090);
        buttonContainer.add(enableScrape);
        buttonContainer.add(disableScrape);
        buttonContainer.setPreferredSize(new Dimension(1000, 80));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1030, 800));
        frame.setLayout(new FlowLayout(0));
        frame.add(mainContainer, BorderLayout.NORTH);
        frame.add(buttonContainer, BorderLayout.SOUTH);
        frame.setTitle("TRACKERBOI 9000");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void set3080Content(ArrayList<String> data) { data3080 = data; }

    public void set3090Content(ArrayList<String> data) {
        data3090 = data;
    }

    public void setUrls(String uri3080, String uri3090) {
        url3080.setText(uri3080);
        url3090.setText(uri3090);
    }

    public ArrayList<String> getUrls() {
        ArrayList<String> ret = new ArrayList<String>();
        ret.add(url3080.getText());
        ret.add(url3090.getText());
        return ret;
    }

    private Component setup3080Panel(JPanel panel3080, UrlTextPane pane3080) {

        //Setup 3080 panel
        TitledBorder title = BorderFactory.createTitledBorder("3080 Tracker");
        title.setTitleJustification(TitledBorder.CENTER);
        title.setTitleColor(Color.BLACK);
        panel3080.setBorder(title); //Set title to the Panel

        panel3080.setLayout(new BorderLayout());

        pane3080.setBorder(BorderFactory.createTitledBorder("3080"));

        panel3080.add(pane3080, BorderLayout.NORTH);

        return panel3080;
    }

    private Component setup3090Panel(JPanel panel3090, UrlTextPane pane3090) {

        //Setup 3090 panel
        TitledBorder title = BorderFactory.createTitledBorder("3090 Tracker");
        title.setTitleJustification(TitledBorder.CENTER);
        title.setTitleColor(Color.BLACK);
        panel3090.setBorder(title); //Set title to the Panel

        panel3090.setLayout(new BorderLayout());

        pane3090.setBorder(BorderFactory.createTitledBorder("3090"));

        panel3090.add(pane3090, BorderLayout.NORTH);

        return panel3090;
    }

    private String convertText(ArrayList<String> data) {

        if (data.size() == 0) {
            return "";
        }

        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern urlPattern = Pattern.compile(regex);

        StringBuilder answer = new StringBuilder();
        answer.append("<html><body>");

        for (String entry : data) {
            String split[] = entry.split("\\|");
            String content = StringEscapeUtils.escapeHtml4(split[1]).replace(" ", "%20");
            int lastIndex = 0;
            Matcher matcher = urlPattern.matcher(content);
            while (matcher.find()) {
                //Append everything since last update to the url:
                answer.append(content.substring(lastIndex, matcher.start()));
                String url = content.substring(matcher.start(), matcher.end()).trim();
                answer.append("<a href=\"" + url + "\">" + split[0] + "</a>");
                lastIndex = matcher.end();
            }
            answer.append(content.substring(lastIndex));
            answer.append("<br><br>");
        }
        //Append end:
        return answer.toString();
    }

    private static class UrlTextPane extends JTextPane {

        public UrlTextPane() {
            this.setEditable(false);
            this.addHyperlinkListener(new UrlHyperlinkListener());
            this.setContentType("text/html");
        }

        private static class UrlHyperlinkListener implements HyperlinkListener {
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(event.getURL().toURI());
                    } catch (IOException e) {
                        throw new RuntimeException("Can't open URL", e);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException("Can't open URL", e);
                    }
                }
            }
        }
    }
}

