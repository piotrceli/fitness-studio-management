package com.junior.company.fitness_studio_management.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:static/api-description.html")
public class SwaggerConstants {


    public static final String API_KEY_REFERENCE = "JWT";
    public static final String AUTHORIZATION_SCOPE = "global";
    public static final String AUTHORIZATION_DESCRIPTION = "full access";
    public static final String API_TITLE = "Fitness Studio Management";

    private static String API_DESCRIPTION;
    @Value("${api.description}")
    private void setApiDescription(String apiDescription) {
        API_DESCRIPTION = apiDescription;
    }
    public static String getApiDescription() {
        return API_DESCRIPTION;
    }

    public static final String API_VERSION = "1.0";
    public static final String API_TERMS_OF_SERVICE_URL = null;
    public static final String CONTACT_NAME = null;
    public static final String CONTACT_URL = null;
    public static final String CONTACT_EMAIL = null;
    public static final String API_LICENSE = null;
    public static final String API_LICENSE_URL = null;
    public static final String USERS_API_TAG = "Users service";
    public static final String FITNESS_CLASSES_API_TAG = "Fitness classes service";
    public static final String GYM_EVENTS_API_TAG = "Gym events service";
    public static final String TRAINERS_API_TAG = "Trainers service";
}
