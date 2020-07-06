#!/usr/bin/env groovy

def call(tests , suite, project,
         host = env.LEAP_HOST, token = env.LEAP_TOKEN,
         passPercentVar = 'PASSED_PERCENTAGE', resultsSourceFile = 'results.properties') {
    def parseError = { res ->
        def msg = res == null ? null : res.message ?: res
        return msg != null ? "\nMSG: ${msg}" : ''
    }
    def info = { String msg -> println "(${project}/${suite}): ${msg}" }
    def debug = { String msg ->
        if (env.DEBUG == 'true' || env.debug == 'true') {
            info msg
        }
    }
    def setPassed = { value ->
        value  = (int) Float.valueOf(value)
        env[passPercentVar] = value
        writeFile file: resultsSourceFile, text: "${passPercentVar}=${value}"
    }

    setPassed 0
    info "starting execution..."

    def executionApi = new LEAPClient(debug, host, token)
    executionApi.updateSuiteTests(tests , suite , project)
    def exec = executionApi.runSuite(suite, project)
    debug exec
    def status, completed = false, retry = 5
    if (exec != null && exec['suiteId'] != null) {
        while (!completed) {
            sleep 5
            status = executionApi.getExecutionStatus(exec.id)
            if (status && status.jobs) {
                completed = status.finishedPercentage == 100
                info "completion(${completed}) - ${status.finishedPercentage}%"
            } else {
                completed = --retry == 0
            }
        }
    } else {
        error "unable to trigger suite - ${suite}" + parseError(exec)
    }
    if (status == null || !status.jobs) {
        error "unable to read status for execution - ${exe.name}" + parseError(status)
    } else {
        info "pass percentage - ${status.passPercentage}%"
        setPassed status.passPercentage ?: 0
    }
    return status
}
