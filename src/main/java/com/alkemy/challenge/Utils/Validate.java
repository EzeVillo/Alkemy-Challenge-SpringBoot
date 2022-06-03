package com.alkemy.challenge.Utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.alkemy.challenge.Error.Exceptions.ValidationException;

public class Validate<T> {
    public void validate(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();
            violations.forEach(x -> mensaje.append(x.getPropertyPath() + ": " + x.getMessage() + ". "));
            throw new ValidationException(mensaje.toString().trim());
        }
    }
}
