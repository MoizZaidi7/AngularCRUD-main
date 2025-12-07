#!/bin/bash
# EC2 Complete Setup Script for User Management System
# Run this script after SSH into your EC2 instance
# Usage: chmod +x ec2-setup.sh && ./ec2-setup.sh

echo "========================================="
echo "EC2 Setup for User Management System"
echo "========================================="

# Update system
echo "Step 1: Updating system packages..."
sudo apt update && sudo apt upgrade -y

# Install Node.js
echo "Step 2: Installing Node.js..."
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs
node --version
npm --version

# Install MongoDB
echo "Step 3: Installing MongoDB..."
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
sudo apt update
sudo apt install -y mongodb-org
sudo systemctl start mongod
sudo systemctl enable mongod
sudo systemctl status mongod

# Install Java 17 (required for Jenkins 2.528+)
echo "Step 4: Installing Java 17..."
sudo apt install -y openjdk-17-jdk
java -version

# Install Maven
echo "Step 5: Installing Maven..."
sudo apt install -y maven
mvn -version

# Install Docker
echo "Step 6: Installing Docker..."
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install -y docker-ce
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ubuntu
docker --version

# Install Jenkins
echo "Step 7: Installing Jenkins..."
# Add Jenkins repository (using recommended method)
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update
sudo apt install -y jenkins

# Configure Jenkins to use Java 17
sudo mkdir -p /etc/systemd/system/jenkins.service.d
sudo tee /etc/systemd/system/jenkins.service.d/override.conf > /dev/null << 'JAVAEOF'
[Service]
Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
Environment="PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:$PATH"
JAVAEOF

sudo systemctl daemon-reload
sudo systemctl start jenkins
sudo systemctl enable jenkins
sudo usermod -aG docker jenkins

# Install PM2
echo "Step 8: Installing PM2..."
sudo npm install -g pm2

# Install Git
echo "Step 9: Installing Git..."
sudo apt install -y git
git --version

echo "========================================="
echo "Installation Complete!"
echo "========================================="
echo ""
echo "Next steps:"
echo "1. Get Jenkins initial password: sudo cat /var/lib/jenkins/secrets/initialAdminPassword"
echo "2. Access Jenkins: http://YOUR_EC2_IP:8080"
echo "3. Complete Jenkins setup wizard (install suggested plugins)"
echo "4. Clone your repository: git clone https://github.com/MoizZaidi7/AngularCRUD-main.git"
echo "5. Run deployment script: ./deploy-app.sh"
echo ""
echo "Important: Make sure port 8080 is open in your EC2 Security Group for Jenkins"
echo "Restart terminal or run: newgrp docker"
