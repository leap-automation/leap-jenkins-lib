#!/usr/bin/groovy
import groovy.json.JsonSlurperClassic
@Grab("org.jodd:jodd-http:5.1.4")
import jodd.http.HttpRequest


class HttpClient {
    def token
    def log

    HttpClient(log, token) {
        this.token = token
        this.log = log
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    get(String url) {
        try {
            log "requesting -\nGET ${url}"
            def res = new HttpRequest().get(url)
                    .tokenAuthentication(token).acceptJson().acceptJson().send().bodyText()
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
            def res = new HttpRequest().post(url)
                    .tokenAuthentication(token).acceptJson().contentTypeJson().body(data).send().bodyText()

            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }
}
