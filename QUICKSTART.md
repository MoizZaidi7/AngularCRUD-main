# Quick Start Guide - User Management System

This guide will help you quickly set up and run the User Management System for your DevOps assignment.

## 🚀 Quick Setup (5 Minutes)

### Step 1: Prerequisites Check

Make sure you have these installed:
```powershell
# Check Node.js
node --version  # Should be v14+

# Check MongoDB
Get-Service -Name MongoDB  # Should be running

# Check npm
npm --version
```

### Step 2: Install & Configure

```powershell
# Navigate to backend folder
cd backend

# Install dependencies
npm install

# Create .env file
Copy-Item .env.example .env

# Edit .env if needed (optional)
notepad .env
```

### Step 3: Start Application

```powershell
# Start backend (from backend folder)
npm run dev

# Application will be available at http://localhost:3000
```

### Step 4: Test in Browser

1. Open browser: `http://localhost:3000`
2. Try adding a user:
   - Name: John Doe
   - Email: john@example.com
   - Age: 25
   - Address: 123 Main St

## 📋 For Assignment Submission

### Part I: Selenium Tests (Already Created!)

Location: `selenium-tests/src/test/java/com/usermanagement/tests/UserManagementSeleniumTests.java`

**12 Test Cases Included:**
✅ TC01: Page Load Verification
✅ TC02: Add User
✅ TC03: Verify User in Table
✅ TC04: Edit User
✅ TC05: Delete User
✅ TC06: Search by Name
✅ TC07: Search by Email
✅ TC08: Form Validation
✅ TC09: Duplicate Email Check
✅ TC10: Refresh Button
✅ TC11: Cancel Edit
✅ TC12: Clear Search

**To run tests:**
```bash
cd selenium-tests
mvn clean test
```

### Part II: Jenkins Pipeline (Already Created!)

Location: `Jenkinsfile`

**Pipeline includes:**
- ✅ Checkout from GitHub
- ✅ Build stage
- ✅ Test stage (runs in Docker)
- ✅ Email notifications

## 🐳 Using Docker (Optional)

### Run with Docker Compose:
```powershell
docker-compose up -d
```

Application available at: `http://localhost:3000`

### Stop:
```powershell
docker-compose down
```

## 📝 Assignment Checklist

- [ ] Application running locally
- [ ] Can add, edit, delete users
- [ ] MongoDB connected and storing data
- [ ] Selenium tests written (12+ tests) ✓ (Already done!)
- [ ] Tests run successfully locally
- [ ] Code pushed to GitHub
- [ ] Jenkins pipeline configured
- [ ] Pipeline triggered by GitHub push
- [ ] Email notifications working
- [ ] Screenshots taken for report
- [ ] Instructor added as collaborator

## 🔧 Common Issues & Solutions

### Issue 1: MongoDB Not Running
```powershell
# Start MongoDB service
Start-Service -Name MongoDB
```

### Issue 2: Port 3000 Already in Use
```powershell
# Find process using port 3000
netstat -ano | findstr :3000

# Kill the process
taskkill /PID <process_id> /F
```

### Issue 3: Dependencies Not Installing
```powershell
# Clear npm cache
npm cache clean --force

# Reinstall
Remove-Item -Recurse -Force node_modules
npm install
```

## 📊 API Endpoints Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/getUsers` | Get all users |
| GET | `/api/users/getUser/:userId` | Get one user |
| POST | `/api/users/addUsers` | Add new user |
| PUT | `/api/users/updateUser/:userId` | Update user |
| DELETE | `/api/users/deleteUser/:userId` | Delete user |

## 🧪 Testing the API (Using PowerShell)

### Get all users:
```powershell
Invoke-RestMethod -Uri "http://localhost:3000/api/users/getUsers" -Method Get
```

### Add a user:
```powershell
$body = @{
    name = "Test User"
    email = "test@example.com"
    age = 25
    address = "Test Address"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:3000/api/users/addUsers" -Method Post -Body $body -ContentType "application/json"
```

## 📸 Screenshots Needed for Report

1. ✅ Application homepage
2. ✅ Add user form
3. ✅ Users table with data
4. ✅ Edit user functionality
5. ✅ Search functionality
6. ✅ MongoDB database (using MongoDB Compass)
7. ✅ Selenium tests running
8. ✅ Test results report
9. ✅ Jenkins pipeline configuration
10. ✅ Jenkins pipeline execution
11. ✅ Email notification received
12. ✅ GitHub repository

## 🌐 Deployment URLs for Form

You'll need to provide:
1. **Deployment URL**: Your EC2 instance URL (e.g., `http://ec2-xx-xx-xx-xx.compute.amazonaws.com:3000`)
2. **Application Code GitHub URL**: `https://github.com/YOUR_USERNAME/user-management-app`
3. **Test Code GitHub URL**: Same repository or separate (include selenium-tests folder)

## 📧 Email Configuration for Jenkins

In Jenkins:
1. Install "Email Extension Plugin"
2. Configure SMTP settings in "Manage Jenkins" → "Configure System"
3. Update Jenkinsfile with correct email addresses

## 🎯 Next Steps

1. **Deploy to AWS EC2:**
   - Launch EC2 instance
   - Install Node.js, MongoDB, Docker, Jenkins
   - Clone repository
   - Configure Jenkins pipeline
   - Setup GitHub webhook

2. **Test Everything:**
   - Push code to GitHub
   - Verify Jenkins pipeline triggers
   - Check email notifications

3. **Create Report:**
   - Document all steps
   - Include screenshots
   - Add Jenkinsfile content
   - Explain test cases

## 💡 Tips for Success

1. **Test Locally First**: Make sure everything works on your local machine
2. **Use Unique Emails**: In Selenium tests, use timestamps to avoid duplicates
3. **Keep Logs**: Screenshot all terminal outputs and logs
4. **Backup Often**: Commit to GitHub frequently
5. **Ask for Help**: Reach out if stuck on Jenkins/AWS setup

## 📚 Additional Resources

- MongoDB Compass (GUI): https://www.mongodb.com/products/compass
- Postman (API Testing): https://www.postman.com/
- Jenkins Documentation: https://www.jenkins.io/doc/
- AWS EC2 Setup: https://aws.amazon.com/ec2/getting-started/

## 🎓 For the Report

Include:
1. **Introduction**: Brief overview of the application
2. **Technology Stack**: List all technologies used
3. **Application Features**: CRUD operations, search, etc.
4. **Database Schema**: User model structure
5. **API Documentation**: Endpoints and examples
6. **Selenium Tests**: Describe each test case
7. **Jenkins Pipeline**: Explain each stage
8. **Deployment**: EC2 setup steps
9. **Results**: Screenshots of successful execution
10. **Conclusion**: Learning outcomes

---

**Good luck with your assignment! 🚀**

For questions or issues, refer to the main README.md or contact your instructor.
