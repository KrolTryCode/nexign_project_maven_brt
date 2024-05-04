DROP TABLE subscribers;
DROP TABLE tariffs;

CREATE TABLE tariffs (
                         id INT PRIMARY KEY,
                         name VARCHAR(255),
                         call_rate_same_operator DECIMAL(10, 2),
                         call_rate_other_operator DECIMAL(10, 2),
                         free_incoming_minutes INT DEFAULT 0,
                         monthly_fee DECIMAL(10, 1) DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS subscribers (
    id SERIAL PRIMARY KEY,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    balance DECIMAL(10, 1) NOT NULL,
    tariff_id INT NOT NULL,
    CONSTRAINT fk_tariff
        FOREIGN KEY (tariff_id)
            REFERENCES tariffs(id)
);

