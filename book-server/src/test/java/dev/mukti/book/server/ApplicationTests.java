package dev.mukti.book.server;

import dev.mukti.grpc.book.lib.FindBookByRequest;
import org.jooq.Row1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.array;
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;

class ApplicationTests {

	@Test
	void contextLoads() {
		var genres = Stream.of("a", "b", "c").map(DSL::row).toArray(Row1[]::new);

		var dslContext = DSL.using(SQLDialect.POSTGRES);
		var sql = dslContext.insertInto(table("genres"), field("genre"))
						.valuesOfRows(genres)
								.getSQL();

		System.out.println(sql);
	}

	@Test
	void where() {
		String title = "a";
		String author = "b";
		List<String> genres = List.of("a", "b");
		var dslContext = DSL.using(SQLDialect.POSTGRES);
		var sql = dslContext.select(asterisk(), field((coalesce(arrayAgg(field(select(field("genre")).from("genres").where(field("book_id").eq("b.id")))), array()))))
				.from(table("books").as("b"))
				.where(field("title").eq(title))
				.and(field("author").eq(author))
				.and(field("id").in(
						select(field("book_id"))
								.from(table("genres"))
								.where(field("genre").in())));
		var proto = FindBookByRequest.newBuilder()
						.addAllGenres(List.of("a", "b", "c"))
								.build();



		System.out.println(DSL.noCondition().and(field("title").eq("a")).and(field("author").eq("b").and(field("id").in(select(field("book_id")).from("genres").where(field("genre").in(proto.getGenresList().toArray()))))));
	}
}
