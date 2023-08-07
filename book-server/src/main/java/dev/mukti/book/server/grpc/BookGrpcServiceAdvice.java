package dev.mukti.book.server.grpc;

import io.grpc.Status;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

@GRpcServiceAdvice
public class BookGrpcServiceAdvice {

    @GRpcExceptionHandler
    public Status handleDuplicateKeyException(DuplicateKeyException exception, GRpcExceptionScope scope) {
        var message = "Resource already exist";
        var psqlException = (PSQLException) exception.getMostSpecificCause();
        if (psqlException.getServerErrorMessage() != null) {
            var constraint = psqlException.getServerErrorMessage().getConstraint();
            message = getMessageByConstraint(constraint);
        }

        return Status.ALREADY_EXISTS.withDescription(message).withCause(exception);
    }

    private String getMessageByConstraint(String constraint) {
        constraint = Optional.ofNullable(constraint).orElse("");
        if (constraint.equals("genres_book_id_genre_key")) {
            return "Book can't have the same genre";
        }

        return "general error";
    }
}
