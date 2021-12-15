FROM node:17-alpine

COPY package.json ./
COPY tsconfig.json ./
COPY src ./src

RUN yarn
RUN yarn build

# COPY out ./

EXPOSE 34343

CMD ["node", "--es-module-specifier-resolution=node", "out/server.js"]
