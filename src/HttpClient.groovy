#!/usr/bin/groovy
import groovy.json.JsonSlurperClassic
@Grab("org.jodd:jodd-http:5.1.4")
import jodd.http.HttpRequest

import java.util.function.Consumer

class HttpClient {
    private String token
    private def log

    HttpClient(log, token) {
        this.token = token
        this.log = log
    }

    static def httpClient() {
        return new HttpRequest()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    Object get(String url) {
        try {
            this.log "requesting -\nGET ${url}"
            def req = httpClient().post(url)
            if (token != null) {
                req = req.tokenAuthentication(token)
            }
            def res = req.acceptJson().acceptJson().send().bodyText()
            this.log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    Object post(String url, String data = "") {
        try {
            this.log "requesting -\nPOST ${url}"
            def req = httpClient().post(url)
            if (token != null) {
                req = req.tokenAuthentication(token)
            }
            def res = req.acceptJson().contentTypeJson().body(data).send().bodyText()
            this.log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }
}
