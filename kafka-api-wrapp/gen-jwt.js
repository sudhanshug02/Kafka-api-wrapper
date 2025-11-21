// gen-jwt.js
const jwt = require('jsonwebtoken');

// Secret key (must match your Spring Boot configuration)
const secret = "ReplaceWithAStrongSecretKeyForJWT";

// Create a JWT valid for 1 hour
const token = jwt.sign({ sub: "admin" }, secret, { expiresIn: '1h' });

// Print the token
console.log("Generated JWT:");
console.log(token);
