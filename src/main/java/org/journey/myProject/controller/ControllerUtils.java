package org.journey.myProject.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        Map<String, String> errorsMap = new HashMap<>();
        for(FieldError fieldError : fieldErrorList) {
            errorsMap.put(fieldError.getField() + "Error", fieldError.getDefaultMessage());
        }
        return errorsMap;
    }

}
