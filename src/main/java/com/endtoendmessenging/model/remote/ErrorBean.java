package com.endtoendmessenging.model.remote;

public class ErrorBean {

    private String message;
    private String suggestion;

    public ErrorBean(String message) {
        this.message = message;
    }

    public ErrorBean(String message, String suggestion) {
        this(message);
        this.suggestion = suggestion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
