CREATE DATABASE music_app;
USE my_database;

-- =====================
-- ROLE
-- =====================
CREATE TABLE Role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

-- =====================
-- USER
-- =====================
CREATE TABLE User (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      username VARCHAR(100) NOT NULL UNIQUE,
                      hash_password VARCHAR(255) NOT NULL,
                      device_token VARCHAR(255),
                      avatar VARCHAR(500),
                      locked BOOLEAN DEFAULT FALSE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      role_id BIGINT,
                      FOREIGN KEY (role_id) REFERENCES Role(id)
);

-- =====================
-- ALBUM
-- =====================
CREATE TABLE Album (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       avatar VARCHAR(500),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================
-- SINGER
-- =====================
CREATE TABLE Singer (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        avatar VARCHAR(500),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================
-- TYPE (Genre)
-- =====================
CREATE TABLE Type (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      avatar VARCHAR(500),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================
-- SONG
-- =====================
CREATE TABLE Song (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      avatar VARCHAR(500),
                      file VARCHAR(500) NOT NULL,
                      lyrics TEXT,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      album_id BIGINT,
                      FOREIGN KEY (album_id) REFERENCES Album(id)
);

-- =====================
-- SONG_SINGER (Many-to-Many)
-- =====================
CREATE TABLE Song_Singer (
                             song_id BIGINT,
                             singer_id BIGINT,
                             PRIMARY KEY (song_id, singer_id),
                             FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE,
                             FOREIGN KEY (singer_id) REFERENCES Singer(id) ON DELETE CASCADE
);

-- =====================
-- SONG_TYPE (Many-to-Many)
-- =====================
CREATE TABLE Song_Type (
                           song_id BIGINT,
                           type_id BIGINT,
                           PRIMARY KEY (song_id, type_id),
                           FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE,
                           FOREIGN KEY (type_id) REFERENCES Type(id) ON DELETE CASCADE
);

-- =====================
-- PLAYLIST
-- =====================
CREATE TABLE Playlist (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          avatar VARCHAR(500),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          user_id BIGINT,
                          FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- =====================
-- PLAYLIST_SONG
-- =====================
CREATE TABLE Playlist_Song (
                               playlist_id BIGINT,
                               song_id BIGINT,
                               PRIMARY KEY (playlist_id, song_id),
                               FOREIGN KEY (playlist_id) REFERENCES Playlist(id) ON DELETE CASCADE,
                               FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE
);

-- =====================
-- COMMENT (self reference)
-- =====================
CREATE TABLE Comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         content TEXT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         user_id BIGINT,
                         song_id BIGINT,
                         parent_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
                         FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE,
                         FOREIGN KEY (parent_id) REFERENCES Comment(id) ON DELETE CASCADE
);

-- =====================
-- LISTEN HISTORY
-- =====================
CREATE TABLE Listen (
                        user_id BIGINT,
                        song_id BIGINT,
                        listen_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (user_id, song_id, listen_time),
                        FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
                        FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE
);

-- =====================
-- USER LOVE SONG
-- =====================
CREATE TABLE User_Love_Song (
                                user_id BIGINT,
                                song_id BIGINT,
                                PRIMARY KEY (user_id, song_id),
                                FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
                                FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE CASCADE
);

-- =====================
-- USER LOVE SINGER
-- =====================
CREATE TABLE User_Love_Singer (
                                  user_id BIGINT,
                                  singer_id BIGINT,
                                  PRIMARY KEY (user_id, singer_id),
                                  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
                                  FOREIGN KEY (singer_id) REFERENCES Singer(id) ON DELETE CASCADE
);

-- =====================
-- NOTIFICATION
-- =====================
CREATE TABLE Notification (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              title VARCHAR(255),
                              content TEXT,
                              status VARCHAR(50),
                              type VARCHAR(50),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              user_id BIGINT,
                              song_id BIGINT,
                              FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
                              FOREIGN KEY (song_id) REFERENCES Song(id) ON DELETE SET NULL
);

-- =====================
-- VERIFICATION TOKEN
-- =====================
CREATE TABLE VerificationToken (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   token VARCHAR(255) NOT NULL UNIQUE,
                                   expiry_time TIMESTAMP NOT NULL,
                                   user_id BIGINT,
                                   FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);