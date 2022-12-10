CREATE TABLE IF NOT EXISTS users(
   id SERIAL PRIMARY KEY,
   name VARCHAR(100),
   last_name VARCHAR(100),
   email VARCHAR(100) UNIQUE,
   login VARCHAR(100) UNIQUE,
   password VARCHAR(100),
   salt VARCHAR(100),
   role VARCHAR(100)
);