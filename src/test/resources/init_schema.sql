CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    amount NUMERIC,
    status VARCHAR(50) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
    );
