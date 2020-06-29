#!/usr/bin/groovy
import groovy.json.JsonSlurperClassic
@Grab("org.jodd:jodd-http:5.1.4")
import jodd.http.HttpRequest

import java.util.function.Consumer

class HttpClient {
    private String token
    private Consumer<GString> log

    HttpClient(log, token) {
        this.token = token
        this.log = log
    }

    static def httpClient() {
        return new HttpRequest()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    get(String url) {
        try {
            log "requesting -\nGET ${url}"
            def req = httpClient().post(url)
            if (token != null) {
                req = req.tokenAuthentication(token)
            }
            def res = req.acceptJson().acceptJson().send().bodyText()
            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    post(String url, String data = "") {
        try {
            log "requesting -\nPOST ${url}"
            def req = httpClient().post(url)
            if (token != null) {
                req = req.tokenAuthentication(token)
            }
            def res = req.acceptJson().contentTypeJson().body(data).send().bodyText()
            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }
}
