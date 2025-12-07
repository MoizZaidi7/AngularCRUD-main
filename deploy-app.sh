#!/bin/bash
# Application Deployment Script
# Run this after cloning your repository

echo "========================================="
echo "Deploying User Management Application"
echo "========================================="

# Get repository name
REPO_DIR="$HOME/AngularCRUD-main"

# Navigate to repository
cd "$REPO_DIR" || { echo "Repository directory not found!"; exit 1; }

# Setup Backend
echo "Step 1: Setting up backend..."
cd backend

# Install dependencies
echo "Installing backend dependencies..."
npm install

# Create .env file
echo "Creating .env file..."
cat > .env << EOL
MONGO_URI=mongodb://localhost:27017/userManagementDB
PORT=3000
EOL

echo ".env file created successfully!"

# Start application with PM2
echo "Step 2: Starting application with PM2..."
pm2 stop user-management-app 2>/dev/null || true
pm2 delete user-management-app 2>/dev/null || true
pm2 start index.js --name user-management-app

# Save PM2 configuration
pm2 save

# Setup PM2 to start on boot
sudo env PATH=$PATH:/usr/bin /usr/lib/node_modules/pm2/bin/pm2 startup systemd -u ubuntu --hp /home/ubuntu

# Show status
pm2 status

echo "========================================="
echo "Deployment Complete!"
echo "========================================="
echo ""
echo "Application Details:"
echo "- URL: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):3000"
echo "- API: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):3000/api/users"
echo "- Status: pm2 status"
echo "- Logs: pm2 logs user-management-app"
echo "- Restart: pm2 restart user-management-app"
echo ""
echo "MongoDB Status:"
sudo systemctl status mongod --no-pager
echo ""
echo "Next: Configure Jenkins pipeline!"
