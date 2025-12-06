package com.example.internal_management_system.common.constants;

public class AppConstants {

    // JWT Constants
    public static final String JWT_SECRET = "internalManagementSystemSecretKeyForJwtTokenGenerationAndValidation123456789";
    public static final long JWT_EXPIRATION = 86400000L; // 24 hours

    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_HR = "HR";
    public static final String ROLE_WAREHOUSE = "WAREHOUSE";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_STAFF = "STAFF";

    // Common Messages
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String OPERATION_SUCCESSFUL = "Operation completed successfully";

    // Pagination Defaults
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Database Constraints
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 500;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_PHONE_LENGTH = 20;

    private AppConstants() {
        // Utility class
    }
}
