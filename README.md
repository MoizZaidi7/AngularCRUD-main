# User Management System

A full-stack CRUD application built with **Express.js**, **MongoDB**, and **Vanilla JavaScript** for the DevOps (CSC483) Assignment 3.

## Features

- ✅ Create, Read, Update, and Delete (CRUD) operations for users
- ✅ RESTful API with Express.js
- ✅ MongoDB database for data persistence
- ✅ Responsive frontend interface
- ✅ Search and filter functionality
- ✅ Real-time form validation
- ✅ Modern UI with gradient design

## Tech Stack

### Backend
- **Node.js** - JavaScript runtime
- **Express.js** - Web framework
- **MongoDB** - NoSQL database
- **Mongoose** - MongoDB ODM
- **CORS** - Cross-Origin Resource Sharing
- **dotenv** - Environment variable management

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling with gradients and animations
- **Vanilla JavaScript** - Frontend logic and API calls

## Project Structure

```
AngularCRUD-main/
├── backend/
│   ├── controllers/
│   │   └── user.controllers.js    # User CRUD operations logic
│   ├── models/
│   │   └── user.model.js          # User schema definition
│   ├── routes/
│   │   └── user.routes.js         # API route definitions
│   ├── index.js                   # Main server file
│   ├── package.json               # Backend dependencies
│   └── .env.example               # Environment variables template
├── frontend/
│   ├── index.html                 # Main HTML file
│   ├── styles.css                 # Stylesheet
│   └── script.js                  # Frontend JavaScript
└── README.md
```

## Prerequisites

Before running this application, make sure you have the following installed:

- **Node.js** (v14 or higher) - [Download here](https://nodejs.org/)
- **MongoDB** (v4.4 or higher) - [Download here](https://www.mongodb.com/try/download/community)
- **Git** - [Download here](https://git-scm.com/)

## Installation & Setup

### Step 1: Clone the Repository

```bash
git clone <your-repo-url>
cd AngularCRUD-main
```

### Step 2: Install MongoDB

#### Windows:
1. Download MongoDB Community Server from [MongoDB Download Center](https://www.mongodb.com/try/download/community)
2. Install MongoDB with default settings
3. MongoDB will run as a Windows service automatically

#### Verify MongoDB is running:
```powershell
# Check if MongoDB service is running
Get-Service -Name MongoDB

# Or start MongoDB manually
mongod
```

### Step 3: Configure Environment Variables

1. Navigate to the backend folder:
```bash
cd backend
```

2. Create a `.env` file by copying the example:
```bash
Copy-Item .env.example .env
```

3. Edit the `.env` file and update if needed:
```env
MONGO_URI=mongodb://localhost:27017/userManagementDB
PORT=3000
```

### Step 4: Install Backend Dependencies

```bash
# Make sure you're in the backend directory
npm install
```

### Step 5: Run the Application

#### Start MongoDB (if not running as service):
```powershell
# In a separate terminal
mongod
```

#### Start the Backend Server:
```bash
# From the backend directory
npm run dev
```

You should see:
```
App is listening at http://localhost:3000
Frontend available at http://localhost:3000
API available at http://localhost:3000/api/users
Connected to the database!
```

### Step 6: Access the Application

Open your browser and navigate to:
```
http://localhost:3000
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/getUsers` | Get all users |
| GET | `/api/users/getUser/:userId` | Get a specific user by ID |
| POST | `/api/users/addUsers` | Create a new user |
| PUT | `/api/users/updateUser/:userId` | Update a user by ID |
| DELETE | `/api/users/deleteUser/:userId` | Delete a user by ID |

## API Request Examples

### Create a User (POST)
```json
POST http://localhost:3000/api/users/addUsers
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "age": 25,
  "address": "123 Main St, City"
}
```

### Get All Users (GET)
```
GET http://localhost:3000/api/users/getUsers
```

### Update a User (PUT)
```json
PUT http://localhost:3000/api/users/updateUser/{userId}
Content-Type: application/json

{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "age": 26,
  "address": "456 New St, City"
}
```

### Delete a User (DELETE)
```
DELETE http://localhost:3000/api/users/deleteUser/{userId}
```

## User Schema

```javascript
{
  name: String (required),
  email: String (required, unique),
  age: Number (required, min: 0),
  address: String (required),
  createdAt: Date (auto-generated),
  updatedAt: Date (auto-generated)
}
```

## Features for Selenium Testing

This application is designed with Selenium testing in mind:

1. **Form Elements**: All form inputs have unique IDs
   - `#name`, `#email`, `#age`, `#address` - Add user form
   - `#updateName`, `#updateEmail`, `#updateAge`, `#updateAddress` - Update form
   - `#searchInput` - Search functionality

2. **Buttons**: All buttons have unique IDs or classes
   - `#submitBtn` - Add/Update user button
   - `#refreshBtn` - Refresh users list
   - `#searchBtn` - Search button
   - `#clearSearchBtn` - Clear search

3. **Table**: Users displayed in a table with ID `#usersTable`

4. **Messages**: 
   - `#successMessage` - Success notifications
   - `#errorMessage` - Error notifications
   - `#loadingSpinner` - Loading indicator

## Sample Test Cases for Selenium

Here are 10+ test case ideas:

1. **TC01**: Verify page loads successfully
2. **TC02**: Add a new user with valid data
3. **TC03**: Verify user appears in the users table
4. **TC04**: Edit an existing user
5. **TC05**: Delete a user
6. **TC06**: Search for a user by name
7. **TC07**: Search for a user by email
8. **TC08**: Verify form validation (empty fields)
9. **TC09**: Verify duplicate email validation
10. **TC10**: Verify refresh button reloads data
11. **TC11**: Verify cancel button in edit mode
12. **TC12**: Verify clear search functionality

## Troubleshooting

### MongoDB Connection Issues

**Error**: `Connection failed!`

**Solutions**:
1. Make sure MongoDB is running:
   ```powershell
   Get-Service -Name MongoDB
   ```

2. Start MongoDB service:
   ```powershell
   Start-Service -Name MongoDB
   ```

3. Or start manually:
   ```bash
   mongod
   ```

4. Check if port 27017 is available:
   ```powershell
   netstat -ano | findstr :27017
   ```

### Port Already in Use

**Error**: `Port 3000 is already in use`

**Solution**:
1. Find the process using port 3000:
   ```powershell
   netstat -ano | findstr :3000
   ```

2. Kill the process:
   ```powershell
   taskkill /PID <process_id> /F
   ```

3. Or change the port in `.env`:
   ```env
   PORT=3001
   ```

### CORS Issues

If you face CORS errors, ensure:
1. The backend has `cors` middleware enabled (already configured)
2. The frontend is accessing the correct API URL in `script.js`

## For Jenkins Pipeline Integration

### Preparing for Selenium Tests

1. **Test Cases**: Write Selenium tests in Java/Python
2. **Headless Chrome**: Use ChromeDriver in headless mode
3. **Docker**: Use `markhobson/maven-chrome` image or create custom Dockerfile
4. **GitHub**: Push code to GitHub repository
5. **Jenkins**: Configure webhook for automated testing

### Sample Dockerfile for Testing

```dockerfile
FROM markhobson/maven-chrome:latest

WORKDIR /app

# Copy test code
COPY selenium-tests/ /app/tests/

# Install dependencies
RUN mvn clean install

# Run tests
CMD ["mvn", "test"]
```

## Contributing

This is an academic project for COMSATS University DevOps course (CSC483).

## License

MIT License

## Author

- **Student Name**: [Your Name]
- **Roll Number**: [Your Roll Number]
- **Course**: CSC483 - Topics in Computer Science II (DevOps)
- **Semester**: Fall 2025

## Acknowledgments

- COMSATS University, Islamabad
- Instructor: Qasim Malik
- Course: BCS-7/BDS-7

---

**Note**: This application is designed for educational purposes as part of Assignment 3 for the DevOps course. It demonstrates CRUD operations, RESTful API design, and is optimized for Selenium automated testing.
