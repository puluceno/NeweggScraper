import groovyx.net.http.HTTPBuilder

static ArrayList<String> scrape(String uri, boolean test) {
    def ret = new ArrayList<String>()
    String AUTO_NOTIFY = "AUTO NOTIFY"
    String SOLD_OUT = "SOLD OUT"
    def http = new HTTPBuilder(uri.replace(" ", "%20"))

    def html = http.get([:])

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