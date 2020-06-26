#!/usr/bin/groovy
import groovy.json.JsonSlurperClassic
@Grab("org.jodd:jodd-http:5.1.4")
import jodd.http.HttpRequest

class LEAPClient {
    private HttpRequest httpRequest = new HttpRequest()
    private String exec_api, token;
    LEAPClient(host, token) {
        this.exec_api = host.endsWith('execution') ? host : host + '/execution'
        this.token = token
    }
  static def httpClient(){
      return new HttpRequest()
  }
    private get(String url) {
        try {
            def res = httpRequest.get(url).tokenAuthentication(token)
                    .acceptJson().send().bodyText()
            return new JsonSlurperClassic().parseText(res)
        } catch (Exception e) {
            return e;
        }
    }

    private post(String url, String data = "") {
        try {
            def res = httpRequest.post(url).tokenAuthentication(token)
                    .acceptJson().contentTypeJson().body(data).send().bodyText()
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
