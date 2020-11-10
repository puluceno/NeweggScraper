import groovyx.net.http.HTTPBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

static ArrayList<String> scrape(String uri, boolean test) {
    def ret = new ArrayList<String>()
    String AUTO_NOTIFY = "AUTO NOTIFY"
    String SOLD_OUT = "SOLD OUT"
    def http = new HTTPBuilder(uri.replace(" ", "%20"))

    def html = http.get([:])

    //Newegg
    html."**".findAll { it.@class.toString().contains("item-container") }.each {
        String product = it.A[0].IMG[0].attributes().get("title").toString()
        String link = it.A[0].attributes().get("href").toString()
        it."**".find { it.@class.toString().contains("item-action") }.each {
            it."**".find { it.@class.toString().contains("item-operate") }.each {
                it."**".find { it.@class.toString().contains("item-button-area") }.each {
                    it."**".find { it.@class.toString().contains("btn") }.each {
                        if (test) {
                            if (it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) || it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                                ret.add(product + "‽" + link)
                            }
                        } else {
                            if (!it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) && !it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                                ret.add(product + "‽" + link)
                            }
                        }
                    }
                }
            }
        }
    }

    return ret
}

static ArrayList<String> scrapeIndividual(String uri, boolean test) {
    def ret = new ArrayList<String>()
    String AUTO_NOTIFY = "AUTO NOTIFY"
    String SOLD_OUT = "SOLD OUT"
    def http = new HTTPBuilder(uri.replace(" ", "%20"))

    def html = http.get([:])

    String product = "";
    String itemLink = "";

    //Newegg
    html."**".findAll { it.@class.toString().contains("item-container") }.each {
        it."**".find { it.@class.toString().contains("item-info") }.each {
            product = it.A[0].text().toString()
            itemLink = it.A[0].attributes().get("href").toString()
        }
        def http2 = new HTTPBuilder(itemLink.replace(" ", "%20"))
        def html2 = http2.get([:])
        html2."**".find { it.@class.toString().contains("btn-wide") }.each {
            if (test) {
                if (it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) || it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                    ret.add(product + "‽" + itemLink)
                }
            } else {
                if (!it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) && !it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                    ret.add(product + "‽" + itemLink)
                }
            }
        }
        html2."**".find { it.@class.toString().contains("atnPrimary") }.each {
            if (test) {
                if (it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) || it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                    ret.add(product + "‽" + itemLink)
                }
            } else {
                if (!it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) && !it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                    ret.add(product + "‽" + itemLink)
                }
            }

        }
    }

    return ret
}


static ArrayList<String> scrapeBestBuy(String uri, boolean test) {

    String linkPrefix = "https://www.bestbuy.com"

    def ret = new ArrayList<String>()
    String AUTO_NOTIFY = "AUTO NOTIFY"
    String SOLD_OUT = "SOLD OUT"

    URL url = new URL(uri.replace(" ", "%20"))
    Document doc = Jsoup.parse(url, 5000)

    Elements prodLink = doc.select("h4[class=sku-header]")

    for (each in prodLink) {

        String link = linkPrefix + each.select("a").first().attributes().get("href")
        String product = each.select("a").first().text()

        Element itemButton = doc.select("div[class=sku-list-item-button]").first()
        String buttonText = itemButton.select("button").first().text()

        if (test) {
            if (buttonText?.toUpperCase()?.contains(AUTO_NOTIFY) || buttonText?.toUpperCase()?.contains(SOLD_OUT)) {
                ret.add(product + "‽" + link)
            }
        } else {
            if (!buttonText?.toUpperCase()?.contains(AUTO_NOTIFY) && !buttonText?.toUpperCase()?.contains(SOLD_OUT)) {
                ret.add(product + "‽" + link)
            }
        }
    }

    return ret
}