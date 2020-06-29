#!/usr/bin/env groovy

def call(projectId, modelId,
         host = env.RBOT_HOST, token = env.RBOT_TOKEN,
         pattern = '--tests {testCaseID}', sep = ' ', envName = 'RBOT_TESTS'
) {
    def parseError = { res ->
        def msg = res == null ? null : res.message ?: res
        return msg != null ? "\nMSG: ${msg}" : ''
    }
    def info = { String msg -> println "RBOT(${projectId}/${modelId}): ${msg}" }
    def debug = { String msg ->
        if (env.DEBUG == 'true' || env.debug == 'true') {
            info msg
        }
    }
    def isCollectionOrArray = { object ->
        return [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
    }

    info "requesting regression tests..."

    def client = new RBotClient(debug, host, token)
    def tests = client.getRecommendedTest(projectId, modelId)

    if (tests == null || !isCollectionOrArray(tests)) {
        error "unable get regression tests" + parseError(tests)
    }
    if (client.isEmpty(tests)) {
        error "no regression tests returned"
    }
    if (pattern != null) {
        tests = client.resolvePattern(pattern, tests)
        debug tests
        if (envName != null)
            env[envName] = tests
    }
    return tests
}