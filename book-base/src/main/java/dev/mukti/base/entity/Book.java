package dev.mukti.base.entity;

import java.time.OffsetDateTime;
import java.util.List;

public record Book(Long id,
                   String title,
                   String author,
                   String description,
                   List<String> genres,
                   OffsetDateTime createdAt,
                   OffsetDateTime updatedAt) {
}
