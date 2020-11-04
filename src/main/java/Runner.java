import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Runner {
    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {

        Scraper scraper = new Scraper();

        while (true) {
            ArrayList<String> urls = scraper.getUrls();
            if (scraper.keepScrapin) {
                if (urls.size() > 0) {
                    ArrayList<String> scrape1 = ScraperHelper.scrape3080(urls.get(0), false);
                    if (scrape1.size() > 0) {
                        URL resource = Runner.class.getResource("alert.wav");
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(Paths.get(resource.toURI()).toFile());
                        clip.open(inputStream);
                        clip.start();
                    }
                    scraper.set3080Content(scrape1);
                }

                if (urls.size() > 1) {
                    ArrayList<String> scrape2 = ScraperHelper.scrape3090(urls.get(1), false);
                    if (scrape2.size() > 0) {
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
                    ArrayList<String> scrape1 = ScraperHelper.scrape3080(urls.get(0), true);
                    scraper.set3080Content(scrape1);
                }

                if (urls.size() > 1) {
                    ArrayList<String> scrape2 = ScraperHelper.scrape3090(urls.get(1), true);
                    scraper.set3090Content(scrape2);
                }
            }
            sleep(5000);
        }
    }
}
