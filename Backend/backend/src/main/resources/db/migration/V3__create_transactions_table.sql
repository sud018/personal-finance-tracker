CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              amount DECIMAL(10,2) NOT NULL,
                              description VARCHAR(255),
                              date DATE NOT NULL,
                              type VARCHAR(10) NOT NULL,
                              user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                              category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);