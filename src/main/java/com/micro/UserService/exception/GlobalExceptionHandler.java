package com.micro.UserService.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(
            UserNotFoundException ex) {

        return createProblem(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {

        return createProblem(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(
            IllegalArgumentException ex) {

        return createProblem(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex) {

        ProblemDetail problem = createProblem(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    error -> error.getField(),
                    error -> error.getDefaultMessage(),
                    (first, second) -> first
                ));

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(
            DataIntegrityViolationException ex) {

        return createProblem(
                HttpStatus.CONFLICT,
                "A database constraint was violated"
        );
    }

    private ProblemDetail createProblem(
            HttpStatus status,
            String message) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status,
                message
        );

        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

}
