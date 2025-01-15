package com.scm.SCMProject.validators;

import jakarta.validation.ConstraintValidator;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5; // 5MB

    @Override
    public boolean isValid(MultipartFile file, jakarta.validation.ConstraintValidatorContext context) {


        if (file.getSize() > MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size exceeds the maximum size of 20MB")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
