#!/usr/bin/groovy
import groovy.json.JsonOutput
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
    doGet(String url) {
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
    doPost(String url, String data = null) {
        try {
            log "requesting -\nPOST ${url}"
            def res = new HttpRequest().post(url)
                    .tokenAuthentication(token).acceptJson().contentTypeJson().body(
                    data == null ? "" : JsonOutput.toJson(data)
            ).send().bodyText()

            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    doPut(String url, def data = null) {
        try {
            log "requesting -\nPUT ${url}"
            def res = new HttpRequest().put(url)
                    .tokenAuthentication(token).acceptJson().contentTypeJson()
                    .body(JsonOutput.toJson(data))
                    .send().bodyText()

            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }
}
