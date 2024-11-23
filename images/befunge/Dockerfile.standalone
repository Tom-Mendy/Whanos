FROM gcc:13.2 AS build

WORKDIR /app

COPY /app /app

COPY /befunge /app

RUN ./compile.sh main.bf executable

FROM ubuntu:24.04 AS standalone

WORKDIR /app

COPY --from=build /app/executable /app/executable

CMD ["/app/executable"]