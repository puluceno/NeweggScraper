package com.scraper;

import com.scraper.helper.ScraperHelper;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.Thread.sleep;

public class Runner {

    private static FileHandler fh;
    private static BufferedInputStream bufferedInputStream;
    private static AudioInputStream audioInputStream;
    private static final Logger logger = Logger.getLogger("pooplog");

    public static void main(String[] args) {
        List<String> scrape3080 = new ArrayList<>();
        List<String> scrape6800xt = new ArrayList<>();
        List<String> scrapeCpu = new ArrayList<>();
        List<String> scrapePs5 = new ArrayList<>();

        try {
            fh = new FileHandler("poop.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            Scraper scraper = new Scraper();

            while (true) {
                List<String> urls = scraper.getUrls();
                if (scraper.keepScraping) {
                    try {
                        if (scraper.scrapeIndividual) {
                            scrape3080 = ScraperHelper.scrapeIndividual(urls.get(0), false);
                            scrape6800xt = ScraperHelper.scrapeIndividual(urls.get(1), false);
                            scrapeCpu = ScraperHelper.scrapeIndividual(urls.get(2), false);
                            scrapePs5 = ScraperHelper.scrapeIndividual(urls.get(3), false);
                        } else {
                            scrape3080 = ScraperHelper.scrape(urls.get(0), false);
                            scrape3080.addAll(ScraperHelper.scrapeBestBuy(urls.get(4), false));

                            scrape6800xt = ScraperHelper.scrape(urls.get(1), false);
                            scrape6800xt.addAll(ScraperHelper.scrapeBestBuy(urls.get(5), false));

                            scrapeCpu = ScraperHelper.scrape(urls.get(2), false);
                            scrapeCpu.addAll(ScraperHelper.scrapeBestBuy(urls.get(6), false));

                            scrapePs5 = ScraperHelper.scrape(urls.get(3), false);
                            scrapePs5.addAll(ScraperHelper.scrapeBestBuy(urls.get(7), false));
                        }
                    } catch (Exception e) {
                        logger.warning("Groovy Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
                    }
                    startAlert(scraper, scrape3080, scrape6800xt, scrapeCpu);
                } else {
                    try {
                        if (scraper.scrapeIndividual) {
                            scrape3080 = ScraperHelper.scrapeIndividual(urls.get(0), true);
                            scrape6800xt = ScraperHelper.scrapeIndividual(urls.get(1), true);
                            scrapeCpu = ScraperHelper.scrapeIndividual(urls.get(2), true);
                            scrapePs5 = ScraperHelper.scrapeIndividual(urls.get(3), true);
                        } else {
                            scrape3080 = ScraperHelper.scrape(urls.get(0), true);
                            scrape3080.addAll(ScraperHelper.scrapeBestBuy(urls.get(4), true));

                            scrape6800xt = ScraperHelper.scrape(urls.get(1), true);
                            scrape6800xt.addAll(ScraperHelper.scrapeBestBuy(urls.get(5), true));

                            scrapeCpu = ScraperHelper.scrape(urls.get(2), true);
                            scrapeCpu.addAll(ScraperHelper.scrapeBestBuy(urls.get(6), true));

                            scrapePs5 = ScraperHelper.scrape(urls.get(3), true);
                            scrapePs5.addAll(ScraperHelper.scrapeBestBuy(urls.get(7), true));
                        }
                    } catch (Exception e) {
                        logger.warning("Groovy Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
                    }
                }
                scraper.set3080Content(scrape3080);
                scraper.set6800xtContent(scrape6800xt);
                scraper.setCpuContent(scrapeCpu);
                scraper.setPs5Content(scrapePs5);
                sleep(5000);
            }
        } catch (Exception e) {
            logger.warning("Main Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
        } finally {
            closer();
        }
    }

    private static void startAlert(Scraper scraper, List<String>... scrapeResult) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        for (List<String> result : scrapeResult) {
            for (String item : result) {
                logger.info(item.split("‽")[0] + " IN STOCK\r\n  " + item.split("‽")[1]);
            }
            if (!scraper.muteSound && !result.isEmpty()) {
                bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(Runner.class.getClassLoader().getResourceAsStream("alert.wav")));
                audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
        }
    }

    public static void closer() {
        try {
            fh.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
            if (audioInputStream != null)
                audioInputStream.close();
        } catch (IOException e) {
            // Do nothing
        }
    }
}
