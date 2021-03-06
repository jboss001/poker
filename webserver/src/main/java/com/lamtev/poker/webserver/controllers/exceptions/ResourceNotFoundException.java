package com.lamtev.poker.webserver.controllers.exceptions;

public class ResourceNotFoundException extends Exception {

    private static final String NOT_FOUND = " not found";

    public ResourceNotFoundException(String resource) {
        super(resource + NOT_FOUND);
    }

}
