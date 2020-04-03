package com.telkomdev.hunk;

public class HunkMethodNotSupportedException extends Exception {

    public HunkMethodNotSupportedException(String method) {
        super(method + " http method is not supported");
    }

    public HunkMethodNotSupportedException(String method, Throwable err) {
        super(method + " http method is not supported", err);
    }
}
