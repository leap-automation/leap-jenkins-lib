#!/usr/bin/env groovy

def call(suite, project,
         host = env.LEAP_HOST, token = env.LEAP_TOKEN,
         passPercentVar = 'PASSED_PERCENTAGE', resultsSourceFile = 'results.properties') {
    def parseError = { res ->
        def msg = res == null ? null : res.message ?: res
        return msg != null ? "\nMSG: ${msg}" : ''
    }
    def log = { String msg -> println "(${project}/${suite}): ${msg}" }
    def debug = { String msg ->
        if (env.DEBUG == 'true' || env.debug == 'true') {
            log msg
        }
    }
    def setPassed = { value ->
        env[passPercentVar] = value
        writeFile file: resultsSourceFile, text: "${passPercentVar}=${value}"
    }

    setPassed 0
    log "starting execution..."

    def executionApi = new LEAPClient(debug, host, token)
    log executionApi
    def exec = executionApi.runSuite(suite, project)
    log exec
    def status, completed = false, retry = 5
    if (exec != null && exec['suiteId'] != null) {
        while (!completed) {
            sleep 5
            status = executionApi.getExecutionStatus(exec.id)
            if (status && status.jobs) {
                completed = status.finishedPercentage == 100
                log "completion(${completed}) - ${status.finishedPercentage}%"
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
        log "pass percentage - ${status.passPercentage}%"
        setPassed status.passPercentage ?: 0
    }
    return status
}