#!/usr/bin/env groovy

def call(projectId, modelId,
         host = env.RBOT_HOST, token = env.RBOT_TOKEN,
         envName = 'RBOT_TESTS', pattern = '--tests {testCaseID}', sep = ' '
) {
    def parseError = { res ->
        def msg = res == null ? null : (res.message != null ? res.message : res)
        return msg != null ? "\nMSG: ${msg}" : ''
    }
    def log = { String msg -> println "RBOT(${projectId}/${modelId}): ${msg}" }
    def debug = { String msg ->
        if (env.DEBUG == 'true' || env.debug == 'true') {
            log msg
        }
    }
    def isCollectionOrArray = { object ->
       return  [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
    }
    def replacePatternWithMap = {  map ->
        pattern.replaceAll(/\{(\w+)\}/){ match , key -> map[key] ?:env[key]}
    }
    log "requesting regression tests..."

    def tests = new RBotClient(debug, host, token)
    if (tests == null || !isCollectionOrArray(tests)){
        error "unable get regression tests" + parseError(tests)
    }
    if (!(test as List)[0]){
        error "no regression tests returned"
    }
    debug tests
    tests = tests.collect { replacePatternWithMap(it) }.join(sep)
    debug tests
    env[envName] = tests
    return tests
}