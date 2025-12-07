# Dockerfile for User Management System

FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy backend files
COPY backend/package*.json ./backend/
COPY backend/ ./backend/

# Copy frontend files
COPY frontend/ ./frontend/

# Install dependencies
WORKDIR /app/backend
RUN npm install

# Expose port
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
