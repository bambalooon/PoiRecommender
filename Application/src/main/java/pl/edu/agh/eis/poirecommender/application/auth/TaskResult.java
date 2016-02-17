package pl.edu.agh.eis.poirecommender.application.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class TaskResult {
    private final boolean success;
    private final String basicAuthToken;

    static TaskResult success(String basicAuthToken) {
        return new TaskResult(true, basicAuthToken);
    }
    static TaskResult fail() {
        return new TaskResult(false, null);
    }
}