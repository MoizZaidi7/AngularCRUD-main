import express from 'express';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import cors from 'cors';
import path from 'path';
import { fileURLToPath } from 'url';
import userRoutes from './routes/user.routes.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

dotenv.config();

mongoose
  .connect(process.env.MONGO_URI)
  .then(() => {
    console.log("Connected to the database!");
  })
  .catch((error) => {
    console.log("Connection failed!", error);
  });

const app = express();

// Middleware
app.use(express.json());
app.use(cors());

// Serve static files from frontend directory
app.use(express.static(path.join(__dirname, '../frontend')));

const port = process.env.PORT || 3000;

// API Routes
app.use("/api/users", userRoutes);

// Serve frontend for root route
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../frontend/index.html'));
});

app.listen(port, () => {
    console.log(`App is listening at http://localhost:${port}`);
    console.log(`Frontend available at http://localhost:${port}`);
    console.log(`API available at http://localhost:${port}/api/users`);
});