import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import static java.lang.Thread.sleep;

public class Runner {

    private static FileHandler fh;

    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {

        Logger logger = Logger.getLogger("pooplog");

        fh = new FileHandler("poop.log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        Scraper scraper = new Scraper();

        while (true) {
            ArrayList<String> urls = scraper.getUrls();
            if (scraper.keepScrapin) {
                if (urls.size() > 0) {
                    ArrayList<String> scrape1 = new ArrayList<String>();
                    try {
                        scrape1 = ScraperHelper.scrape(urls.get(0), false);
                    } catch (Exception e) {
                        logger.warning("Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
                    }
                    if (scrape1.size() > 0) {
                        for (String item : scrape1) {
                            logger.info(item.split("\\|")[0] + " IN STOCK\r\n      " + item.split("\\|")[1]);
                        }
                        URL resource = Runner.class.getResource("alert.wav");
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(Paths.get(resource.toURI()).toFile());
                        clip.open(inputStream);
                        clip.start();
                    }
                    scraper.set3080Content(scrape1);
                }

                if (urls.size() > 1) {
                    ArrayList<String> scrape2 = new ArrayList<String>();
                    try {
                        scrape2 = ScraperHelper.scrape(urls.get(1), false);
                    } catch (Exception e) {
                        //logger.warning("Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
                    }
                    if (scrape2.size() > 0) {
                        for (String item : scrape2) {
                            logger.info(item.split("\\|")[0] + " IN STOCK\r\n      " + item.split("\\|")[1]);
                        }
                        URL resource = Runner.class.getResource("alert.wav");
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(Paths.get(resource.toURI()).toFile());
                        clip.open(inputStream);
                        clip.start();
                    }
                    scraper.set3090Content(scrape2);
                }
            } else {
                if (urls.size() > 0) {
                    ArrayList<String> scrape1 = new ArrayList<String>();
                    try {
                        scrape1 = ScraperHelper.scrape(urls.get(0), true);
                    } catch (Exception e) {
                        logger.warning("Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
                    }
                    scraper.set3080Content(scrape1);
                }

                if (urls.size() > 1) {
                    ArrayList<String> scrape2 = new ArrayList<String>();
                    try {
                        scrape2 = ScraperHelper.scrape(urls.get(1), true);
                    } catch (Exception e) {
                        logger.warning("Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
                    }
                    scraper.set3090Content(scrape2);
                }
            }
            sleep(5000);
        }
    }

    public static void closeFH() {
        fh.close();
    }
}
