#!/usr/bin/groovy
import groovy.json.JsonSlurperClassic
@Grab("org.jodd:jodd-http:5.1.4")
import jodd.http.HttpRequest

class LEAPClient {
    private String exec_api, token;
    private log;
    LEAPClient(log,host, token) {
        this.exec_api = host.endsWith('execution') ? host : host + '/execution'
        this.token = token
        this.log = log
    }
  static def httpClient(){
      return new HttpRequest()
  }
    private get(String url) {
        try {
            log "requesting -\nGET ${url}"
            def res = new HttpRequest().get(url).tokenAuthentication(token)
                    .acceptJson().send().bodyText()
            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e;
        }
    }

    private post(String url, String data = "") {
        try {
            log "requesting -\nPOST ${url}"
            def res = new HttpRequest().post(url).tokenAuthentication(token)
                    .acceptJson().contentTypeJson().body(data).send().bodyText()
            log "response -\n${res}"
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e;
        }
    }

    private getRunSuiteUrl(project, suite) {
        return "${exec_api}/suites/execute?name=${suite}&projectName=${project}"
    }

    private getExecutionStatusUrl(id) {
        return "${exec_api}/executions/${id}/suite-report"
    }

    def runSuite(String suite, String project) {
        return post(getRunSuiteUrl(project, suite))
    }

    def getExecutionStatus(String id) {
        return get(getExecutionStatusUrl(id))
    }
}
