def call(token = null) {
    def log = { String msg -> println "(HttpClient) : ${msg}" }
    def debug = { String msg ->
        if (env.DEBUG == 'true' || env.debug == 'true') {
            log msg
        }
    }
    return new HttpClient(debug , token);
}