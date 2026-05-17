CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS embedded_chunks (

    id BIGSERIAL PRIMARY KEY,

    file_name TEXT NOT NULL,

    source_path TEXT NOT NULL,

    checksum TEXT NOT NULL,

    chunk_index INTEGER NOT NULL,

    snippet_text TEXT NOT NULL,

    embedding vector(1024),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);