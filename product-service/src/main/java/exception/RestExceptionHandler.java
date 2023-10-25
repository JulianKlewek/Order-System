package exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;


import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class RestExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, WebRequest request) {

        log.error(exception.getMessage());

        String errorMessage = "Invalid body";
        List<String> detailedMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();

//        private final Clock clock = Clock.systemUTC()
        StandardError error = StandardError.builder()
//                .timestamp(ZonedDateTime.now(clock))
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error(errorMessage)
                .detailedMessage(detailedMessage)
                .path(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }
}
