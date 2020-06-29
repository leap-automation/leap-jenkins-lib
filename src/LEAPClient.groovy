#!/usr/bin/groovy
class LEAPClient extends  HttpClient{
    private String exec_api

    LEAPClient(log, host, token) {
        super(log,token)
        this.exec_api = host.endsWith('execution') ? host : host + '/execution'
    }

    private getRunSuiteUrl(project, suite) {
        return "${exec_api}/suites/execute?name=${suite}&projectName=${project}"
    }

    private getExecutionStatusUrl(id) {
        return "${exec_api}/executions/${id}/suite-report"
    }

    def runSuite(String suite, String project) {
        return super.post(getRunSuiteUrl(project, suite))
    }

    def getExecutionStatus(String id) {
        return super.get(getExecutionStatusUrl(id))
    }
}
