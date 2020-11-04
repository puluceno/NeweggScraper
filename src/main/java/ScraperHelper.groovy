import groovyx.net.http.HTTPBuilder

// this takes about 125ms
static ArrayList<String> scrape3080(String uri, boolean test) {
    //3080
    def found = false
    def ret = new ArrayList<String>();
    String AUTO_NOTIFY = "AUTO NOTIFY"
    def http = new HTTPBuilder(uri)

    def html = http.get([:])

    html."**".findAll { it.@class.toString().contains("item-container") }.each {
        String product = it.A[0].IMG[0].attributes().get("title").toString()
        String link = it.A[0].attributes().get("href").toString()
        //System.out.println("Product: " + it.A[0].IMG[0].attributes().get("title").toString())
        html."**".find { it.@class.toString().contains("item-action") }.each {
            html."**".find { it.@class.toString().contains("item-operate") }.each {
                html."**".find { it.@class.toString().contains("item-button-area") }.each {
                    if (test) {
                        if (it.BUTTON[0].attributes().get("title").toString()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("Product: " + it.A[0].IMG[0].attributes().get("title").toString())
                            found = true
                        }
                        if (!found && it.BUTTON[0].text()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("Product: " + it.A[0].IMG[0].attributes().get("title").toString())
                            found = true
                        }
                    } else {
                        if (!it.BUTTON[0].attributes().get("title").toString()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("Product: " + it.A[0].IMG[0].attributes().get("title").toString())
                            found = true
                        }
                        if (!found && !it.BUTTON[0].text()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("Product: " + it.A[0].IMG[0].attributes().get("title").toString())
                            found = true
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

// this takes about 125ms
static ArrayList<String> scrape3090(String uri, boolean test) {
    //3090
    def found = false
    def ret = new ArrayList<String>();
    String AUTO_NOTIFY = "AUTO NOTIFY"
    def http = new HTTPBuilder(uri)

    def html = http.get([:])

    html."**".findAll { it.@class.toString().contains("item-container") }.each {
        String product = it.A[0].IMG[0].attributes().get("title").toString()
        String link = it.A[0].attributes().get("href").toString()
        //System.out.println("Product: " + it.A[0].IMG[0].attributes().get("title").toString())
        html."**".find { it.@class.toString().contains("item-action") }.each {
            html."**".find { it.@class.toString().contains("item-operate") }.each {
                html."**".find { it.@class.toString().contains("item-button-area") }.each {
                    if (test) {
                        if (it.BUTTON[0].attributes().get("title").toString()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("    STATUS: Button Title: " + it.BUTTON[0].attributes().get("title").toString() + "; Text: " + it.BUTTON[0].text())
                            found = true
                        }
                        if (!found && it.BUTTON[0].text()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("    STATUS: Button Title: " + it.BUTTON[0].attributes().get("title").toString() + "; Text: " + it.BUTTON[0].text())
                            found = true
                        }
                    } else {
                        if (!it.BUTTON[0].attributes().get("title").toString()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("    STATUS: Button Title: " + it.BUTTON[0].attributes().get("title").toString() + "; Text: " + it.BUTTON[0].text())
                            found = true
                        }
                        if (!found && !it.BUTTON[0].text()?.toUpperCase().contains(AUTO_NOTIFY)) {
                            //System.out.println("    STATUS: Button Title: " + it.BUTTON[0].attributes().get("title").toString() + "; Text: " + it.BUTTON[0].text())
                            found = true
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