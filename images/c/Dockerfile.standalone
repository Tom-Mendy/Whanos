FROM gcc:13.2 AS build

WORKDIR /app

COPY . /app

RUN make

FROM ubuntu:24.04 AS standalone

WORKDIR /app

COPY --from=build /app/compiled-app /app/compiled-app

CMD ["/app/compiled-app"]
