CREATE TABLE budgets (
                         id BIGSERIAL PRIMARY KEY,
                         amount DECIMAL(10,2) NOT NULL,
                         month INTEGER NOT NULL,
                         year INTEGER NOT NULL,
                         user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                         category_id BIGINT REFERENCES categories(id) ON DELETE CASCADE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);