# Stage 1: Build the application
FROM node:18-alpine AS build

# Set working directory to /app for initial setup
WORKDIR /app

# Copy package.json and package-lock.json from the tennis-time folder
COPY tennis-time/package*.json ./

# Install dependencies
RUN npm install

# Copy the entire tennis-time project
COPY tennis-time/ ./

# Build the application using Nx
RUN npx nx reset && npx nx build frontend --prod

# Stage 2: Serve the application using Nginx
FROM nginx:alpine

# Copy the custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built frontend files from the previous stage
COPY --from=build /app/dist/apps/frontend /usr/share/nginx/html

# Expose the port
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
