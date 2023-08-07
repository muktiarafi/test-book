package dev.mukti.book.client.dto;

import dev.mukti.grpc.book.lib.CreateBookRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String author;

    @NotBlank
    @Size(max = 255)
    private String description;

    @NotNull
    private List<String> genres;

    public CreateBookRequest toCreateBookRequest() {
        return CreateBookRequest.newBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setDescription(description)
                .addAllGenres(genres)
                .build();
    }
}
