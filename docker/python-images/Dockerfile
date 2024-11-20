FROM python:3.12 AS dependencies

WORKDIR /app

COPY . /app

RUN pip install -r requirements.txt

FROM dependencies AS standalone

WORKDIR /app

COPY --from=dependencies /app/app /app

CMD ["python", "-m", "app"]
