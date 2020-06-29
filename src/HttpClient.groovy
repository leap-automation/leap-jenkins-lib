#!/usr/bin/groovy
import groovy.json.JsonSlurperClassic
@Grab("org.jodd:jodd-http:5.1.4")
import jodd.http.HttpRequest


class HttpClient {
    private String token
    def log

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
            def res;
            if (token != null) {
               res =   new HttpRequest().get(url).tokenAuthentication(token).acceptJson().acceptJson().send().bodyText()
            }else{
                res =   new HttpRequest().get(url).acceptJson().acceptJson().send().bodyText()
            }
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
            def res ;
            if (token != null) {
                res = new HttpRequest().post(url).tokenAuthentication(token).acceptJson().contentTypeJson().body(data).send().bodyText()
            }else{
                res = new HttpRequest().post(url).acceptJson().contentTypeJson().body(data).send().bodyText()
            }
            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e
        }
    }
}
