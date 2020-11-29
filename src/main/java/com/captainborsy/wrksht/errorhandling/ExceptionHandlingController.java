package com.captainborsy.wrksht.errorhandling;

import com.captainborsy.wrksht.errorhandling.domain.ErrorResponseDTO;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;
import com.captainborsy.wrksht.errorhandling.exception.ConflictException;
import com.captainborsy.wrksht.errorhandling.exception.CredentialException;
import com.captainborsy.wrksht.errorhandling.exception.EntityNotFoundException;
import com.captainborsy.wrksht.errorhandling.exception.InvalidOperationException;
import com.captainborsy.wrksht.errorhandling.exception.InvalidTokenException;
import com.captainborsy.wrksht.errorhandling.exception.NoUserInContextException;
import com.captainborsy.wrksht.errorhandling.exception.UnprocessableEntityException;
import com.captainborsy.wrksht.errorhandling.exception.UserAlreadyLoggedInException;
import com.captainborsy.wrksht.errorhandling.exception.WorkflowStatusChangingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(WrkshtErrors.ACCESS_DENIED, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public Object handleConflictException(WrkshtException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getError(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({CredentialException.class, NoUserInContextException.class})
    public Object handleCredentialException(WrkshtException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getError(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleEntityNotFoundException(WrkshtException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getError(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyLoggedInException.class, WorkflowStatusChangingException.class})
    public Object handleUserAlreadyLoggedInException(WrkshtException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getError(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public Object handleInvalidOperationException(WrkshtException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getError(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public Object handleInvalidTokenException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(WrkshtErrors.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public Object handleUnprocessableEntity(WrkshtException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(ex.getError(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
