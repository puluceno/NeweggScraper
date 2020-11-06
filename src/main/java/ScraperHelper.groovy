import groovyx.net.http.HTTPBuilder

static ArrayList<String> scrape(String uri, boolean test) {
    //3080
    def found = false
    def ret = new ArrayList<String>();
    String AUTO_NOTIFY = "AUTO NOTIFY"
    String SOLD_OUT = "SOLD OUT"
    def http = new HTTPBuilder(uri)

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
                                found = true
                            }
                        } else {
                            if (!it.text()?.toUpperCase()?.contains(AUTO_NOTIFY) && !it.text()?.toUpperCase()?.contains(SOLD_OUT)) {
                                found = true
                            }
                        }
                    }
                }
            }
        }
        if (found) {
            ret.add(product + "|" + link)
        }
    }

    return ret
}