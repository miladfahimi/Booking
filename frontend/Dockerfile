# Stage 1: Build the application
FROM node:18-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the package.json and package-lock.json files to install dependencies
COPY tennis-time/package*.json ./

# Install dependencies using npm
RUN npm install

# Copy the entire project
COPY tennis-time/ ./

# Build the application using Nx (with npx)
RUN npx nx build frontend --prod

# Stage 2: Serve the application using Nginx
FROM nginx:alpine

# Copy the custom nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built application from the previous stage to the Nginx web server directory
COPY --from=build /app/dist/apps/frontend /usr/share/nginx/html

# Expose the port Nginx will serve on
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
