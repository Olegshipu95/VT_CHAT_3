package subscription.exception;

public enum ErrorCode {
    // common errors
    INVALID_REQUEST,
    SERVICE_UNAVAILABLE,

    VALIDATION_FAILED,
    USER_NOT_FOUND,
    SUBSCRIPTION_NOT_FOUND,
    SUBSCRIPTION_ALREADY_EXISTS,
    // user errors
    FORBIDDEN,
}