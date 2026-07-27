package io.github.lazyimmortal.sesame.rpc.request;

public class Request {
    public final String type;
    public final String method;
    public final Object data;

    public Request(String type) {
        this.type = type;
        this.method = null;
        this.data = null;
    }

    public Request(String type, Object data) {
        this.type = type;
        this.method = null;
        this.data = data;
    }

    public Request(String type, String method, Object data) {
        this.type = type;
        this.method = method;
        this.data = data;
    }

    public Request(RequestType requestType) {
        this.type = requestType.name();
        this.method = null;
        this.data = null;
    }

    public Request(RequestType requestType, RequestMethod requestMethod) {
        this.type = requestType.name();
        this.method = requestMethod.name();
        this.data = null;
    }

    public Request(RequestType requestType, Object requestData) {
        this.type = requestType.name();
        this.method = null;
        this.data = requestData;
    }

    public Request(RequestType requestType, RequestMethod requestMethod, Object requestData) {
        this.type = requestType.name();
        this.method = requestMethod.name();
        this.data = requestData;
    }
}