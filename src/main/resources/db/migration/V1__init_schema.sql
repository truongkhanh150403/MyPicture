-- MyPictureApp - Initial schema
-- Target: TiDB Cloud Serverless (MySQL-compatible, TiDB v7+)
-- Flyway naming convention: V1__init_schema.sql
-- Charset: utf8mb4 cho hỗ trợ đầy đủ Unicode (emoji, tiếng Việt có dấu, ...)

-- ============================================================
-- 1. users
-- ============================================================
CREATE TABLE users (
    id           CHAR(36)     NOT NULL,
    username     VARCHAR(50)  NOT NULL,
    password     VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    email        VARCHAR(255) NULL,
    avatar_url   VARCHAR(500) NULL,
    avatar_id    VARCHAR(255) NULL,
    bio          VARCHAR(500) NULL,
    created_at   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================================
-- 2. friend_requests
-- ============================================================
CREATE TABLE friend_requests (
    id           CHAR(36)     NOT NULL,
    from_user_id CHAR(36)     NOT NULL,
    to_user_id   CHAR(36)     NOT NULL,
    status       ENUM('PENDING', 'ACCEPTED', 'DECLINED', 'CANCELED') NOT NULL DEFAULT 'PENDING',
    created_at   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    KEY idx_fr_from_user (from_user_id),
    KEY idx_fr_to_user (to_user_id),
    CONSTRAINT fk_fr_from_user FOREIGN KEY (from_user_id) REFERENCES users (id),
    CONSTRAINT fk_fr_to_user   FOREIGN KEY (to_user_id)   REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================================
-- 3. friends
-- ============================================================
CREATE TABLE friends (
    id         CHAR(36)    NOT NULL,
    user_a_id  CHAR(36)    NOT NULL,
    user_b_id  CHAR(36)    NOT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY uk_friends_pair (user_a_id, user_b_id),
    KEY idx_friends_user_b (user_b_id),
    CONSTRAINT fk_friends_user_a FOREIGN KEY (user_a_id) REFERENCES users (id),
    CONSTRAINT fk_friends_user_b FOREIGN KEY (user_b_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================================
-- 4. posts
-- ============================================================
CREATE TABLE posts (
    id         CHAR(36)     NOT NULL,
    poster_id  CHAR(36)     NOT NULL,
    image_url  VARCHAR(500) NOT NULL,
    image_id   VARCHAR(255) NOT NULL,
    created_at DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    KEY idx_posts_poster (poster_id),
    CONSTRAINT fk_posts_poster FOREIGN KEY (poster_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================================
-- 5. refresh_tokens
-- ============================================================
CREATE TABLE refresh_tokens (
    id          CHAR(36)     NOT NULL,
    user_id     CHAR(36)     NOT NULL,
    token_hash  VARCHAR(255) NOT NULL,
    expires_at  DATETIME(3)  NOT NULL,
    revoked     TINYINT(1)   NOT NULL DEFAULT 0,
    created_at  DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY uk_refresh_tokens_hash (token_hash),
    KEY idx_refresh_tokens_user (user_id),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
