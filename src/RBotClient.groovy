#!/usr/bin/groovy
class RBotClient extends HttpClient {
    private String host;

    RBotClient(log, host, token) {
        super(log,host)
        this.host = host
    }

    private getRecommendedTestsUrl(String projectId , String modelId) {
        return "${host}/systemRecommendedTcs?projectid=${projectId}&modelid=${modelId}"
    }

    def getRecommendedTest(projectId, modelId) {
        return get(getRecommendedTestsUrl(projectId, modelId))
    }
}
