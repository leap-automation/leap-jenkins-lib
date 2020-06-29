#!/usr/bin/groovy

class RBotClient extends HttpClient {
    private String host

    RBotClient(log, host, token) {
        super(log, token)
        this.host = host
    }

    private getRecommendedTestsUrl(String projectId, String modelId) {
        return "${host}/systemRecommendedTcs?projectid=${projectId}&modelid=${modelId}"
    }

    def getRecommendedTest(projectId, modelId) {
        return super.doGet(getRecommendedTestsUrl(projectId, modelId))
    }

    def isEmpty(list) {
        list == []
    }

    def resolvePattern(pattern, list, sep = ' ') {
        log pattern
        log list
        list.collect { map ->
            log map
            pattern.replaceAll(/\{(\w+)\}/) { match, key ->
                log match+" :"+key+" :"+ map[key]
                map[key]
            }
        }.join(sep)
    }
}
