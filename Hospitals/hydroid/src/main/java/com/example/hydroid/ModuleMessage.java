package com.example.hydroid;

public class ModuleMessage {
    public enum Status {
        OK("OK"), ERROR("Error"), CLASS_NOT_FOUND_EXCEPTION("Class not found"), JSON_EXCEPTION("JSON error"),
        ILLEGAL_ACCESS("Illegal access"), INVALID_ACTION("Invalid Action"),
        PERMISSION_NOT_GRANTED("Permission Not Granted"),ACTION_CANCELED("Action Canceled");
        private String statusMessage;

        Status(String message) {
            this.statusMessage = message;
        }

        public String getStatusMessage() {
            return statusMessage;
        }
    }


    private int statusID;
    private String callbackID;
    private String encodedMessage;
    private boolean keepOnPipe = false;

    public int getStatusID() {
        return statusID;
    }

    public boolean getKeepOnPipe() {
        return keepOnPipe;
    }

    public String getEncodedMessage() {
        return encodedMessage;
    }

    public String getCallbackID() {
        return callbackID;
    }

    public void setEncodedMessage(String encodedMessage) {
        this.encodedMessage = encodedMessage;
    }

    public void setKeepOnPipe(Boolean value) {
        this.keepOnPipe = value;
    }

    public void setCallbackID(String callbackID) {
        this.callbackID = callbackID;
    }

    public ModuleMessage(Status status) {
        statusID = status.ordinal();
        encodedMessage = status.getStatusMessage();

    }

    public ModuleMessage(Status status, String message) {
        statusID = status.ordinal();
        encodedMessage = message;
    }

    public ModuleMessage(Status status, int integer) {
        statusID = status.ordinal();
        encodedMessage = "" + integer;
    }

    public ModuleMessage(Status status, boolean bool) {
        statusID = status.ordinal();
        encodedMessage = "" + bool;
    }

}
