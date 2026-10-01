
CREATE TABLE customer (

    id UUID NOT NULL,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL,

    CONSTRAINT pk_customer PRIMARY KEY (id),

    CONSTRAINT uk_customer_email UNIQUE (email)
);

INSERT INTO customer (
    id,
    first_name,
    last_name,
    email,
    phone,
    status,
    created_at,
    updated_at
)
VALUES
    (
        gen_random_uuid(),
        'John',
        'Smith',
        'john.smith@example.com',
        '+59891234567',
        'ACTIVE',
        '2026-09-01 10:30:00',
        '2026-09-01 10:30:00'
    ),
    (
        gen_random_uuid(),
        'Alice',
        'Brown',
        'alice.brown@example.com',
        '+59898765432',
        'ACTIVE',
        '2026-09-02 14:15:00',
        '2026-09-01 10:30:00'
    ),
    (
        gen_random_uuid(),
        'Michael',
        'Wilson',
        'michael.wilson@example.com',
        '+59891222333',
        'INACTIVE',
        '2026-09-03 09:00:00',
        '2026-09-01 10:30:00'
    );