CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE colaborador (
        id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
        nome VARCHAR(100) NOT NULL
);