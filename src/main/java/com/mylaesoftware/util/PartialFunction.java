package com.mylaesoftware.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by Claudio Scandura on 20/11/2015.
 * cl.scandura@gmail.com
 */
public class PartialFunction<Domain, Codomain> implements Function<Domain, Codomain> {

    private final List<Handler<Domain, Codomain>> handlers = new LinkedList<>();
    private Optional<Function<Domain, Codomain>> defaultHandler = Optional.empty();

    @Override
    public Codomain apply(Domain domain) {
        return lift().apply(domain).orElseGet(() -> {
            throw new ApplicationUndefinedException();
        });
    }

    public Function<Domain, Optional<Codomain>> lift() {
        return domain ->
            Stream.concat(handlers.stream(), streamDefaultHandler())
                    .filter(e -> e.predicate.test(domain))
                    .findFirst()
                    .map(entry -> entry.function.apply(domain));
    }

    private Stream<Handler<Domain, Codomain>> streamDefaultHandler() {
        return defaultHandler
                .map(handler -> Stream.of(new Handler<>((p) -> true, handler)))
                .orElse(Stream.empty());
    }

    public boolean isDefinedAt(Domain domain) {
        return lift().apply(domain).isPresent();
    }

    public PartialFunction<Domain, Codomain> caseOf(Predicate<Domain> domainValue, Function<Domain, Codomain> function) {
        handlers.add(new Handler<>(domainValue, function));
        return this;
    }

    public PartialFunction<Domain, Codomain> caseDefault(Function<Domain, Codomain> function) {
        defaultHandler = Optional.ofNullable(function);
        return this;
    }

    private static class Handler<Domain, Codomain> {
        final Predicate<Domain> predicate;
        final Function<Domain, Codomain> function;

        Handler(Predicate<Domain> predicate, Function<Domain, Codomain> function) {
            this.predicate = predicate;
            this.function = function;
        }
    }

    static class ApplicationUndefinedException extends RuntimeException {
    }
}
