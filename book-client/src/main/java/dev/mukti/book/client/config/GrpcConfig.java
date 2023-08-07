package dev.mukti.book.client.config;

import dev.mukti.grpc.book.lib.BookServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public BookServiceGrpc.BookServiceBlockingStub bookGrpcStub(
            @Value("${grpc.book.service.host}") String host,
            @Value("${grpc.book.service.port}") Integer port
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        return BookServiceGrpc.newBlockingStub(channel);
    }
}
