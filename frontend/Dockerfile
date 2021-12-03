# build environment
FROM node:14-alpine as build
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json ./
# COPY package-lock.json ./
RUN apk add --update --no-cache curl py-pip make g++
RUN yarn
# RUN npm ci --silent
# RUN npm ci
# RUN npm install react-scripts@3.4.1 -g --silent
# RUN yarn install react-scripts@3.4.1
COPY . ./
RUN yarn build

# production environment
FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
