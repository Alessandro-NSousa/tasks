CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuario (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     nome VARCHAR(100),
     email VARCHAR(100) ,
     password VARCHAR(250),
     role TEXT
);