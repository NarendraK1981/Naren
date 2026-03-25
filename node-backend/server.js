const express = require('express');
const https = require('https');
const fs = require('fs');
const crypto = require('crypto');
const sqlite3 = require('sqlite3').verbose();
const jwt = require('jsonwebtoken');
const JWT_SECRET = 'your-very-secure-secret';
const app = express();
const port = 3443;

// Enable CORS for all routes
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    if (req.method === 'OPTIONS') {
        return res.sendStatus(200);
    }
    next();
});

app.use(express.json());

// In-memory store for OTPs
const otpStore = new Map();
const EXPIRATION_TIME = 60000; // 1 minute

// SQLite database for credentials
const db = new sqlite3.Database('./users.db');

db.serialize(() => {
    db.run(`CREATE TABLE IF NOT EXISTS users (
        email TEXT PRIMARY KEY,
        hash TEXT NOT NULL,
        salt TEXT NOT NULL
    )`);
});

function hashPassword(password, salt) {
    return crypto.pbkdf2Sync(password, salt, 100000, 64, 'sha512').toString('hex');
}

function createUser(email, password, callback) {
    const salt = crypto.randomBytes(16).toString('hex');
    const hash = hashPassword(password, salt);
    db.run('INSERT INTO users (email, hash, salt) VALUES (?, ?, ?)', [email, hash, salt], callback);
}

function verifyUser(email, password, callback) {
    db.get('SELECT hash, salt FROM users WHERE email = ?', [email], (err, row) => {
        if (err) return callback(err);
        if (!row) return callback(null, false);
        const hash = hashPassword(password, row.salt);
        callback(null, crypto.timingSafeEqual(Buffer.from(hash, 'hex'), Buffer.from(row.hash, 'hex')));
    });
}

// Generate OTP Endpoint
app.post('/generate-otp', (req, res) => {
    const { email } = req.body;
    if (!email) {
        return res.status(400).json({ error: "Email is required" });
    }

    const otp = Math.floor(100000 + Math.random() * 900000).toString();

    otpStore.set(email, {
        otp: otp,
        timestamp: Date.now(),
        attempts: 0
    });

    console.log(`Generated OTP for ${email}: ${otp}`);

    res.json({ message: `OTP generated successfully : ${otp}`, success: true });
});

// Validate OTP Endpoint
app.post('/validate-otp', (req, res) => {
    const { email, otp } = req.body;

    if (!email || !otp) {
        return res.status(400).json({ error: "Email and OTP are required" });
    }

    const data = otpStore.get(email);

    if (!data) {
        return res.status(404).json({ error: "OTP not generated" });
    }

    // Check expiration
    if (Date.now() - data.timestamp > EXPIRATION_TIME) {
        otpStore.delete(email);
        return res.status(410).json({ error: "OTP expired" });
    }

    // Check max attempts
    if (data.attempts >= 3) {
        otpStore.delete(email);
        return res.status(403).json({ error: "Maximum attempts exceeded" });
    }

    data.attempts++;

    // Validate OTP
    if (data.otp === otp) {
        otpStore.delete(email);
        res.json({ message: "OTP validated successfully", success: true });
    } else {
        res.status(401).json({ error: "Invalid OTP", attemptsLeft: 3 - data.attempts, success: false });
    }
});

// User registration endpoint (stores password hash + salt)
app.post('/register', (req, res) => {
    const { email, password } = req.body;
    if (!email || !password) {
        return res.status(400).json({ error: 'Email and password are required' });
    }

    createUser(email, password, (err) => {
        if (err) {
            if (err.code === 'SQLITE_CONSTRAINT_PRIMARYKEY') {
                return res.status(409).json({ error: 'User already exists' });
            }
            return res.status(500).json({ error: 'Database error', detail: err.message });
        }

        res.status(201).json({ message: 'User registered successfully' });
    });
});

// Login endpoint (credential check against DB)
app.post('/login', (req, res) => {
    const { email, password } = req.body;
    if (!email || !password) {
        return res.status(400).json({ error: 'Email and password are required' });
    }

    verifyUser(email, password, (err, valid) => {
        if (err) {
            return res.status(500).json({ error: 'Database error', detail: err.message });
        }

        if (!valid) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }

        const token = jwt.sign({ email }, JWT_SECRET, { expiresIn: '1h' });
        res.json({ message: 'Login successful', token, expiresIn: 3600 });
    });
});

app.get('/', (req, res) => {
    res.send("OTP Server is UP and RUNNING!");
});

function requireAuth(req, res, next) {
    const token = req.headers['authorization']?.replace('Bearer ', '');
    if (!token) {
        return res.status(401).json({ error: 'Access denied' });
    }

    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(401).json({ error: 'Invalid token' });
        }
        req.user = decoded;
        next();
    });
}
app.get('/private', requireAuth, (req,res)=>res.json({msg:'ok'}));

app.listen(port, () => {
    console.log(`OTP Service running at http://localhost:${port}`);
});
