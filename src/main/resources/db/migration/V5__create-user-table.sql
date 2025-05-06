CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuario (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email VARCHAR(100) ,
    password VARCHAR(250)
);