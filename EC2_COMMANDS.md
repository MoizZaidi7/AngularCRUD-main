# Complete EC2 Deployment Commands - Copy & Paste Guide

## STEP 1: SSH into EC2 Instance

# Windows (PowerShell)
ssh -i "your-key.pem" ubuntu@YOUR_EC2_PUBLIC_IP

# Example:
# ssh -i "devops-assignment-key.pem" ubuntu@54.123.45.67

## STEP 2: Update System
sudo apt update && sudo apt upgrade -y

## STEP 3: Install Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs
node --version
npm --version

## STEP 4: Install MongoDB
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
sudo apt update
sudo apt install -y mongodb-org
sudo systemctl start mongod
sudo systemctl enable mongod
sudo systemctl status mongod

## STEP 5: Install Java (for Jenkins & Selenium)
sudo apt install -y openjdk-11-jdk
java -version

## STEP 6: Install Maven
sudo apt install -y maven
mvn -version

## STEP 7: Install Docker
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install -y docker-ce
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ubuntu
docker --version

## STEP 8: Install Jenkins
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt update
sudo apt install -y jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins

## STEP 9: Get Jenkins Initial Password
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
# Copy this password - you'll need it to access Jenkins

## STEP 10: Install PM2 (Process Manager)
sudo npm install -g pm2

## STEP 11: Install Git
sudo apt install -y git
git --version

## STEP 12: Clone Your Repository
cd ~
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd YOUR_REPO

# Example:
# git clone https://github.com/john-doe/user-management-app.git
# cd user-management-app

## STEP 13: Setup Backend
cd backend
npm install

## STEP 14: Create .env File
cat > .env << 'EOL'
MONGO_URI=mongodb://localhost:27017/userManagementDB
PORT=3000
EOL

cat .env  # Verify content

## STEP 15: Start Application with PM2
pm2 start index.js --name user-management-app
pm2 save
pm2 startup systemd

# Copy and run the command that PM2 outputs

## STEP 16: Check Application Status
pm2 status
pm2 logs user-management-app

## STEP 17: Get EC2 Public IP
curl http://169.254.169.254/latest/meta-data/public-ipv4

## STEP 18: Test Application
# Open browser: http://YOUR_EC2_IP:3000

## STEP 19: Access Jenkins
# Open browser: http://YOUR_EC2_IP:8080
# Use the password from Step 9

## STEP 20: Configure Jenkins

# Install Required Plugins in Jenkins UI:
# - Docker Pipeline
# - Email Extension Plugin
# - GitHub Plugin
# - JUnit Plugin

## STEP 21: Configure Email in Jenkins
# Manage Jenkins → Configure System → Extended E-mail Notification
# SMTP Server: smtp.gmail.com
# Port: 587
# Use TLS: Yes
# Add credentials (Gmail + App Password)

## STEP 22: Create Jenkins Pipeline Job
# New Item → Pipeline → OK
# Configure:
# - GitHub project URL
# - Pipeline script from SCM
# - SCM: Git
# - Repository URL: https://github.com/YOUR_USERNAME/YOUR_REPO.git
# - Branch: */main or */master
# - Script Path: Jenkinsfile

## STEP 23: Setup GitHub Webhook
# Go to GitHub Repository → Settings → Webhooks → Add webhook
# Payload URL: http://YOUR_EC2_IP:8080/github-webhook/
# Content type: application/json
# Events: Just the push event

## STEP 24: Test Pipeline
# Make a small change to README.md
# Commit and push
# Watch Jenkins automatically trigger

## USEFUL COMMANDS

# Check all services status
sudo systemctl status mongod --no-pager
sudo systemctl status jenkins --no-pager
pm2 status

# View logs
pm2 logs user-management-app
sudo journalctl -u jenkins -f
sudo tail -f /var/log/mongodb/mongod.log

# Restart services
pm2 restart user-management-app
sudo systemctl restart mongod
sudo systemctl restart jenkins

# Stop application
pm2 stop user-management-app

# Check ports
sudo netstat -tulpn | grep -E '3000|8080|27017'

# Check disk space
df -h

# Check memory
free -h

# Check CPU
htop  # Install first: sudo apt install htop

# Pull latest code from GitHub
cd ~/YOUR_REPO
git pull origin main
pm2 restart user-management-app

# Docker commands
docker ps
docker images
docker logs <container_id>

# Test MongoDB connection
mongosh
# In mongosh:
show dbs
use userManagementDB
show collections
db.users.find()
exit

## SECURITY CHECKLIST

# Enable firewall (optional, EC2 Security Groups are primary)
sudo ufw status
sudo ufw allow 22
sudo ufw allow 80
sudo ufw allow 3000
sudo ufw allow 8080
sudo ufw enable

# Update packages regularly
sudo apt update && sudo apt upgrade -y

# Check for vulnerabilities
npm audit
npm audit fix

## TROUBLESHOOTING

# If MongoDB won't start
sudo systemctl status mongod
sudo journalctl -u mongod -f

# If port 3000 is in use
sudo lsof -i :3000
sudo kill -9 <PID>

# If Jenkins won't start
sudo systemctl status jenkins
sudo journalctl -u jenkins -f

# If application can't connect to MongoDB
# Check MongoDB is running
sudo systemctl status mongod
# Check connection string in .env
cat ~/YOUR_REPO/backend/.env

# Reset Jenkins password
sudo cat /var/lib/jenkins/secrets/initialAdminPassword

# View PM2 startup script
pm2 startup

## FINAL VERIFICATION

# Check application is running
curl http://localhost:3000

# Check API endpoint
curl http://localhost:3000/api/users/getUsers

# Check from outside EC2
curl http://YOUR_EC2_PUBLIC_IP:3000

# Test adding a user via API
curl -X POST http://localhost:3000/api/users/addUsers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "age": 25,
    "address": "Test Address"
  }'

## ASSIGNMENT SUBMISSION

# Your URLs should be:
# - Deployment URL: http://YOUR_EC2_IP:3000
# - GitHub Repo: https://github.com/YOUR_USERNAME/YOUR_REPO
# - Jenkins: http://YOUR_EC2_IP:8080

# Add instructor as collaborator on GitHub
# Test pipeline by making a commit
# Verify email notifications work
# Take screenshots of everything
# Submit Google form!
