# Use the official Node.js image to build the application
FROM node:16 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Use the official Nginx image to serve the application
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
