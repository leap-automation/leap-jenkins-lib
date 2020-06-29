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
    def replacePatternWithMap = { map ->
        pattern.replaceAll(/\{(\w+)\}/) { match, key -> map[key] ?: env[key] }
    }
    info "requesting regression tests..."

    def tests = new RBotClient(debug, host, token).getRecommendedTest(projectId,modelId)

    if (tests == null || !isCollectionOrArray(tests)) {
        error "unable get regression tests" + parseError(tests)
    }
    if (!(tests as List)[0]) {
        error "no regression tests returned"
    }
    info tests
    if (pattern != null) {
        tests = tests.collect { replacePatternWithMap(it) }.join(sep)
        debug tests
        if (envName != null)
            env[envName] = tests
    }
    return tests
}