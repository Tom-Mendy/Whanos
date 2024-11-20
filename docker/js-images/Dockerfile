FROM node:20.9 AS dependencies

WORKDIR /app

COPY package.json /app

RUN npm install

COPY . /app

FROM dependencies AS standalone

WORKDIR /app

COPY --from=dependencies /app/app /app

CMD ["node", "."]