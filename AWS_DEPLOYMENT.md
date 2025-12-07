# AWS EC2 Deployment Guide

Complete guide for deploying the User Management System on AWS EC2 with Jenkins.

## 📋 Prerequisites

- AWS Account
- SSH client (PuTTY for Windows or built-in SSH)
- Your application code on GitHub

## 🚀 Part 1: Launch EC2 Instance

### Step 1: Launch Instance

1. **Login to AWS Console**: https://console.aws.amazon.com/
2. **Navigate to EC2**: Services → EC2
3. **Launch Instance**:
   - Click "Launch Instance"
   - **Name**: `user-management-server`
   - **AMI**: Ubuntu Server 22.04 LTS (Free tier eligible)
   - **Instance Type**: `t2.micro` (Free tier)
   - **Key Pair**: Create new or use existing
     - Name: `devops-assignment-key`
     - Type: RSA
     - Format: `.pem` (for SSH) or `.ppk` (for PuTTY)
     - **Download and save securely**

### Step 2: Configure Security Group

Create security group with following rules:

| Type | Protocol | Port Range | Source | Description |
|------|----------|------------|--------|-------------|
| SSH | TCP | 22 | Your IP | SSH access |
| HTTP | TCP | 80 | 0.0.0.0/0 | HTTP access |
| Custom TCP | TCP | 3000 | 0.0.0.0/0 | Application |
| Custom TCP | TCP | 8080 | 0.0.0.0/0 | Jenkins |

### Step 3: Launch & Connect

1. **Launch Instance** (wait for status: Running)
2. **Note Public IP**: e.g., `54.123.45.67`
3. **Connect via SSH**:

**Using Windows PowerShell:**
```powershell
# Set correct permissions (if using .pem file)
icacls "devops-assignment-key.pem" /inheritance:r
icacls "devops-assignment-key.pem" /grant:r "$($env:USERNAME):(R)"

# Connect
ssh -i "devops-assignment-key.pem" ubuntu@54.123.45.67
```

**Using PuTTY (Windows):**
- Host: `ubuntu@54.123.45.67`
- Port: 22
- Auth: Browse to your `.ppk` file

## 🔧 Part 2: Install Required Software

### Step 1: Update System

```bash
sudo apt update && sudo apt upgrade -y
```

### Step 2: Install Node.js

```bash
# Install Node.js 18.x
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs

# Verify installation
node --version
npm --version
```

### Step 3: Install MongoDB

```bash
# Import MongoDB public GPG key
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -

# Create list file
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

# Update and install
sudo apt update
sudo apt install -y mongodb-org

# Start MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod

# Verify
sudo systemctl status mongod
```

### Step 4: Install Java (for Jenkins & Selenium)

```bash
# Install Java 11
sudo apt install -y openjdk-11-jdk

# Verify
java -version
```

### Step 5: Install Maven

```bash
sudo apt install -y maven

# Verify
mvn -version
```

### Step 6: Install Docker

```bash
# Install Docker
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install -y docker-ce

# Start and enable Docker
sudo systemctl start docker
sudo systemctl enable docker

# Add user to docker group
sudo usermod -aG docker ubuntu
sudo usermod -aG docker jenkins

# Verify
docker --version
```

### Step 7: Install Jenkins

```bash
# Add Jenkins repository
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'

# Install Jenkins
sudo apt update
sudo apt install -y jenkins

# Start Jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Get initial admin password
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

## 🌐 Part 3: Deploy Application

### Step 1: Clone Repository

```bash
# Navigate to home directory
cd ~

# Clone your repository
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd YOUR_REPO

# Install backend dependencies
cd backend
npm install
```

### Step 2: Configure Environment

```bash
# Create .env file
nano .env

# Add the following:
MONGO_URI=mongodb://localhost:27017/userManagementDB
PORT=3000

# Save: Ctrl+O, Enter, Ctrl+X
```

### Step 3: Install PM2 (Process Manager)

```bash
# Install PM2 globally
sudo npm install -g pm2

# Start application with PM2
pm2 start index.js --name user-management-app

# Save PM2 configuration
pm2 save

# Setup PM2 to start on boot
pm2 startup systemd
# Run the command it outputs
```

### Step 4: Verify Application

```bash
# Check if app is running
pm2 status

# View logs
pm2 logs user-management-app

# Test locally
curl http://localhost:3000
```

### Step 5: Access from Browser

Open browser: `http://YOUR_EC2_PUBLIC_IP:3000`

## 🔨 Part 4: Configure Jenkins

### Step 1: Initial Setup

1. **Access Jenkins**: `http://YOUR_EC2_PUBLIC_IP:8080`
2. **Enter initial admin password** (from Step 7 above)
3. **Install suggested plugins**
4. **Create admin user**:
   - Username: admin
   - Password: (choose strong password)
   - Email: your-email@example.com

### Step 2: Install Required Plugins

Navigate to: Manage Jenkins → Manage Plugins → Available

Install these plugins:
- ✅ Docker Pipeline
- ✅ Email Extension Plugin
- ✅ GitHub Plugin
- ✅ Pipeline
- ✅ Git Plugin
- ✅ JUnit Plugin

**Restart Jenkins**: `sudo systemctl restart jenkins`

### Step 3: Configure Email Notifications

1. **Go to**: Manage Jenkins → Configure System
2. **Scroll to**: Extended E-mail Notification
3. **Configure SMTP**:
   - SMTP Server: `smtp.gmail.com`
   - SMTP Port: `587`
   - Use SSL: ✅
   - Credentials: Add (Gmail + App Password)
4. **Test**: Send test email

### Step 4: Create Pipeline Job

1. **New Item** → Name: `user-management-pipeline` → Pipeline → OK
2. **Configure**:
   - Description: "Automated testing pipeline for User Management System"
   - ✅ GitHub project: `https://github.com/YOUR_USERNAME/YOUR_REPO`
   - ✅ Poll SCM: `H/5 * * * *` (every 5 minutes)

3. **Pipeline**:
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: `https://github.com/YOUR_USERNAME/YOUR_REPO.git`
   - Credentials: Add your GitHub token
   - Branch: `*/main` or `*/master`
   - Script Path: `Jenkinsfile`

4. **Save**

### Step 5: Configure GitHub Webhook

1. **Go to GitHub Repository** → Settings → Webhooks
2. **Add webhook**:
   - Payload URL: `http://YOUR_EC2_PUBLIC_IP:8080/github-webhook/`
   - Content type: `application/json`
   - Events: ✅ Just the push event
   - Active: ✅
3. **Add webhook**

## ✅ Part 5: Test the Pipeline

### Step 1: Make a Test Commit

```bash
# On your local machine
cd your-repo
echo "# Test" >> README.md
git add .
git commit -m "Test Jenkins pipeline"
git push origin main
```

### Step 2: Monitor Jenkins

1. **Go to Jenkins Dashboard**
2. **Watch pipeline trigger automatically**
3. **View console output**
4. **Check email for notification**

### Step 3: Verify Test Results

In Jenkins job:
- View test results
- Download test reports
- Check email notifications

## 🔍 Troubleshooting

### Issue 1: Jenkins Can't Access Docker

```bash
# Add Jenkins to Docker group
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins

# Verify
sudo -u jenkins docker ps
```

### Issue 2: Application Not Accessible

```bash
# Check if app is running
pm2 status

# Check firewall
sudo ufw status
sudo ufw allow 3000

# Check security group in AWS
```

### Issue 3: MongoDB Connection Failed

```bash
# Check MongoDB status
sudo systemctl status mongod

# Start if not running
sudo systemctl start mongod

# Check connection
mongo --eval "db.version()"
```

### Issue 4: Jenkins Pipeline Fails

```bash
# Check Jenkins logs
sudo tail -f /var/log/jenkins/jenkins.log

# Check Docker logs
docker logs <container_id>
```

## 📧 Email Configuration (Gmail)

### Option 1: Using Gmail (Recommended)

1. **Enable 2-Step Verification** on your Google Account
2. **Generate App Password**:
   - Google Account → Security → 2-Step Verification → App passwords
   - App: Mail
   - Device: Jenkins
   - Copy the 16-character password

3. **Jenkins Configuration**:
   - SMTP Server: `smtp.gmail.com`
   - Port: `587`
   - Username: `your-email@gmail.com`
   - Password: App Password (16 characters)
   - Use TLS: ✅

### Option 2: Using AWS SES (Alternative)

See AWS SES documentation for setup.

## 🔐 Security Best Practices

1. **Change default passwords**
2. **Use security groups** to restrict access
3. **Enable HTTPS** (use Let's Encrypt)
4. **Keep software updated**:
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```
5. **Use environment variables** for sensitive data
6. **Don't commit `.env` files** to Git

## 📊 Monitoring & Maintenance

### Monitor Application

```bash
# PM2 monitoring
pm2 monit

# View logs
pm2 logs

# Restart app
pm2 restart user-management-app
```

### Monitor System

```bash
# CPU and Memory
htop

# Disk usage
df -h

# Network
netstat -tulpn
```

### Backup MongoDB

```bash
# Create backup
mongodump --db userManagementDB --out /home/ubuntu/backups/

# Restore backup
mongorestore --db userManagementDB /home/ubuntu/backups/userManagementDB/
```

## 📝 URLs for Assignment Submission

After successful deployment, you'll have:

1. **Deployment URL**: `http://YOUR_EC2_PUBLIC_IP:3000`
2. **Jenkins URL**: `http://YOUR_EC2_PUBLIC_IP:8080`
3. **GitHub Repository**: `https://github.com/YOUR_USERNAME/YOUR_REPO`

Submit these URLs in the Google Form!

## 🎯 Final Checklist

- [ ] EC2 instance running
- [ ] All software installed
- [ ] Application deployed and accessible
- [ ] MongoDB connected
- [ ] Jenkins installed and configured
- [ ] GitHub webhook configured
- [ ] Pipeline runs successfully
- [ ] Email notifications working
- [ ] Instructor added as GitHub collaborator
- [ ] URLs submitted in Google Form

## 💡 Tips

1. **Use Elastic IP** for permanent IP address
2. **Enable CloudWatch** for monitoring
3. **Set up alerts** for system issues
4. **Regular backups** of database
5. **Document everything** for your report

## 📞 Need Help?

- Check Jenkins console output
- Review application logs: `pm2 logs`
- Check system logs: `/var/log/syslog`
- Verify security group rules
- Test locally first

---

**Congratulations!** 🎉 Your application is now deployed on AWS EC2 with Jenkins CI/CD pipeline!

For questions, refer to:
- README.md - Application documentation
- QUICKSTART.md - Quick commands
- PROJECT_SUMMARY.md - Project overview
