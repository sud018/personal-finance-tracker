CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            color VARCHAR(20),
                            icon VARCHAR(50),
                            type VARCHAR(10) NOT NULL,
                            user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);