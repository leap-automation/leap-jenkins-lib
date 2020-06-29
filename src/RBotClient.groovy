#!/usr/bin/groovy
class RBotClient extends HttpClient {
    private String host;

    RBotClient(log, host, token) {
        super(log,token)
        this.host = host
    }

    private getRecommendedTestsUrl(String projectId , String modelId) {
        return "${host}/systemRecommendedTcs?projectid=${projectId}&modelid=${modelId}"
    }

    def getRecommendedTest(projectId, modelId) {
        return super.doGet(getRecommendedTestsUrl(projectId, modelId))
    }

    def isEmpty(list){
        super.log "checking is empty"
        return list == []
    }

    def resolvePattern(pattern, list, sep = ' '){
        super.log "resolving pattern: "+pattern
        def replacePatternWithMap = { map ->
            pattern.replaceAll(/\{(\w+)\}/) { match, key -> map[key] }
        }
        return list.collect { replacePatternWithMap(it) }.join(sep)
    }
}
