package ua.nure.cherkashyn.hotel.web.service.entity;

public class AuthenticationChecker {

    private boolean isEmailValid;
    private boolean isPasswordValid;
    private boolean isError;

    private String emailMessage;
    private String passwordMessage;
    private String errorMessage;

    private String linkToRedirect;

    public AuthenticationChecker() {
        isEmailValid = true;
        isPasswordValid = true;
    }

    public boolean isEmailValid() {
        return isEmailValid;
    }

    public void setEmailValid(boolean emailValid) {
        isEmailValid = emailValid;
    }

    public boolean isPasswordValid() {
        return isPasswordValid;
    }

    public void setPasswordValid(boolean passwordValid) {
        isPasswordValid = passwordValid;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String getPasswordMessage() {
        return passwordMessage;
    }

    public void setPasswordMessage(String passwordMessage) {
        this.passwordMessage = passwordMessage;
    }

    public String getLinkToRedirect() {
        return linkToRedirect;
    }

    public void setLinkToRedirect(String linkToRedirect) {
        this.linkToRedirect = linkToRedirect;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
