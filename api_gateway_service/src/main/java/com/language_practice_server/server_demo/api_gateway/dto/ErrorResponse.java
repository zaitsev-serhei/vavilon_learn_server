package com.language_practice_server.server_demo.api_gateway.dto;

public final class ErrorResponse {
    final private String timeStamp;
    final private int status;
    final private String error;
    final private String path;

    public ErrorResponse(String timeStamp, int status, String error, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
