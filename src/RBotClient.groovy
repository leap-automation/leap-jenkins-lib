#!/usr/bin/groovy
class RBotClient extends HttpClient {
    private String host;

    RBotClient(log, host, token) {
        super(log,host)
        this.host = host
    }

    private getRecommendedTestsUrl(projectId, modelId) {
        return "${host}/systemRecommendedTcs?projectid=${projectId}&modelid=${modelId}"
    }

    def getRecommendedTest(String id) {
        return get(getRecommendedTestsUrl(projectId, modelId))
    }
}
