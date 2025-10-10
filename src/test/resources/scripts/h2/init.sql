DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS credential (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    passwordHash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS patientProfile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    birthDate DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    highContrast BOOLEAN NOT NULL DEFAULT FALSE,
    bigFont BOOLEAN NOT NULL DEFAULT FALSE,
    reminderCalendar BOOLEAN NOT NULL DEFAULT FALSE,
    reminderCalendarSchedule TIME,
    reminderWorkout BOOLEAN NOT NULL DEFAULT FALSE,
    reminderWorkoutSchedule TIME,
    encouragingMessages BOOLEAN NOT NULL DEFAULT TRUE,
    workoutMediaType VARCHAR(10) NOT NULL DEFAULT 'VIDEO'
);

-- Tabela principal de usuários
CREATE TABLE IF NOT EXISTS appUser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    profilePictureUrl VARCHAR(255) DEFAULT NULL,
    credentialId BIGINT NOT NULL,
    patientProfileId BIGINT NOT NULL,
    preferencesId BIGINT NOT NULL,
    FOREIGN KEY (credentialId) REFERENCES credential(id),
    FOREIGN KEY (patientProfileId) REFERENCES patientProfile(id),
    FOREIGN KEY (preferencesId) REFERENCES preferences(id)
);

-- Tabela principal de questões com campos adicionais
CREATE TABLE IF NOT EXISTS question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    externalId VARCHAR(50),
    text VARCHAR(10000) NOT NULL,
    type VARCHAR(10) NOT NULL,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    placeholder VARCHAR(255),
    minValue INT,
    maxValue INT,
    step INT
);

-- Tabela para armazenar as opções das questões (para RADIO e CHECKBOX)
CREATE TABLE IF NOT EXISTS questionOption (
    id INT AUTO_INCREMENT PRIMARY KEY,
    questionId BIGINT,
    label VARCHAR(255) NOT NULL,
    textValue VARCHAR(50) NOT NULL,
    FOREIGN KEY (questionId) REFERENCES question(id)
);

-- Tabela para armazenar registros para o diário miccional
CREATE TABLE IF NOT EXISTS calendarDay (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dateValue DATE NOT NULL,
    userId BIGINT NOT NULL,
    leakageLevel VARCHAR(20) NOT NULL,
    eventsCount INT NOT NULL DEFAULT 0,
    completedExercises INT NOT NULL DEFAULT 0,
    notesPreview VARCHAR(10000),
    dayTitle VARCHAR(10) NOT NULL,
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

CREATE TABLE IF NOT EXISTS urinationData (
    id INT AUTO_INCREMENT PRIMARY KEY,
    calendarDayId INT NOT NULL,
    timeValue TIME NOT NULL,
    amount VARCHAR(20) NOT NULL,
    leakage BOOLEAN NOT NULL DEFAULT FALSE,
    reason VARCHAR(10000),
    urgency BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (calendarDayId) REFERENCES calendarDay(id)
);

CREATE TABLE IF NOT EXISTS media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    contentType VARCHAR(10) NOT NULL,
    contentSize BIGINT NOT NULL,
    altText VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS exercise (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(10000),
    category INT NOT NULL,
    intensityLevel INT NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS exerciseMedia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);

CREATE TABLE IF NOT EXISTS exerciseFeedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR(10000),
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

CREATE TABLE IF NOT EXISTS contentCategory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    auditable BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(10000) NOT NULL,
    subtitle VARCHAR(255),
    subContent VARCHAR(10000),
    categoryId INT NOT NULL,
    authorId BIGINT,
    repost BOOLEAN NOT NULL DEFAULT FALSE,
    repostFromcontentId BIGINT DEFAULT NULL,
    repostByAuthorId BIGINT DEFAULT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (repostByAuthorId) REFERENCES appUser(id),
    FOREIGN KEY (categoryId) REFERENCES contentCategory(id)
);

CREATE TABLE IF NOT EXISTS comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contentId BIGINT NOT NULL,
    authorId BIGINT NOT NULL,
    text VARCHAR(10000) NOT NULL,
    reply BOOLEAN NOT NULL DEFAULT FALSE,
    replyToCommentId BIGINT DEFAULT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (replyToCommentId) REFERENCES comment(id)
);

CREATE TABLE IF NOT EXISTS contentLikes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

CREATE TABLE IF NOT EXISTS commentLikes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    commentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (commentId) REFERENCES comment(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

CREATE TABLE IF NOT EXISTS contentMedia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contentId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);