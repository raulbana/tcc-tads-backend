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
    iciq3answer INT DEFAULT 0,
    iciq4answer INT DEFAULT 0,
    iciq5answer INT DEFAULT 0,
    iciqScore INT DEFAULT 0,
    urinationLoss VARCHAR(26) DEFAULT NULL,
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

CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    permissionLevel INT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    hasDocument BOOLEAN NOT NULL DEFAULT FALSE,
    documentType VARCHAR(50),
    documentValue VARCHAR(255),
    conceivedBy BIGINT DEFAULT NULL,
    conceivedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
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
    roleId INT NOT NULL,
    strikes INT NOT NULL DEFAULT 0,
    blocked BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (credentialId) REFERENCES credential(id),
    FOREIGN KEY (patientProfileId) REFERENCES patientProfile(id),
    FOREIGN KEY (preferencesId) REFERENCES preferences(id),
    FOREIGN KEY (roleId) REFERENCES role(id)
);

ALTER TABLE role
ADD CONSTRAINT FK_ConceivedBy FOREIGN KEY (conceivedBy) REFERENCES appUser(id);

-- Tabela para armazenar códigos OTP para recuperação de senha
CREATE TABLE IF NOT EXISTS OTP (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    code VARCHAR(255) NOT NULL,
    expirationTime BIGINT NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (userId) REFERENCES appUser(id)
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

-- Diário miccional
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

-- Mídia
CREATE TABLE IF NOT EXISTS media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    contentType VARCHAR(10) NOT NULL,
    contentSize BIGINT NOT NULL,
    altText VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

-- Exercícios / Treinos
CREATE TABLE IF NOT EXISTS exerciseAttribute (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS exerciseCategory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS exercise (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    categoryId INT NOT NULL,
    instructions VARCHAR(10000) NOT NULL,
    repetitions INT NOT NULL,
    sets INT NOT NULL,
    restTime INT NOT NULL,
    duration INT NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (categoryId) REFERENCES exerciseCategory(id)
);

CREATE TABLE IF NOT EXISTS exerciseExerciseAttribute (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    attributeId INT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (attributeId) REFERENCES exerciseAttribute(id)
);

CREATE TABLE IF NOT EXISTS exerciseMedia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);

CREATE TABLE IF NOT EXISTS workout (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(10000) NOT NULL,
    totalDuration INT NOT NULL,
    difficultyLevel VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS workoutExercise (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workoutId BIGINT NOT NULL,
    exerciseId BIGINT NOT NULL,
    exerciseOrder INT NOT NULL,
    FOREIGN KEY (workoutId) REFERENCES workout(id),
    FOREIGN KEY (exerciseId) REFERENCES exercise(id)
);

CREATE TABLE IF NOT EXISTS workoutPlan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(10000) DEFAULT NULL,
    daysPerWeek INT NOT NULL,
    totalWeeks INT NOT NULL,
    iciqScoreMin INT NOT NULL,
    iciqScoreMax INT NOT NULL,
    ageMin INT NOT NULL,
    ageMax INT NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS workoutPlanWorkout (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workoutPlanId BIGINT NOT NULL,
    workoutId BIGINT NOT NULL,
    workoutOrder INT NOT NULL,
    FOREIGN KEY (workoutPlanId) REFERENCES workoutPlan(id),
    FOREIGN KEY (workoutId) REFERENCES workout(id)
);

CREATE TABLE IF NOT EXISTS userWorkoutPlan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    workoutPlanId BIGINT NOT NULL,
    startDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    endDate TIMESTAMP DEFAULT NULL,
    progress INT DEFAULT NULL,
    currentWeek INT NOT NULL DEFAULT 1,
    nextWorkout INT DEFAULT NULL,
    lastWorkoutDate TIMESTAMP DEFAULT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    UNIQUE (userId, workoutPlanId),
    FOREIGN KEY (userId) REFERENCES appUser(id),
    FOREIGN KEY (workoutPlanId) REFERENCES workoutPlan(id)
);

CREATE TABLE IF NOT EXISTS workoutFeedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workoutId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    evaluation VARCHAR(20) NOT NULL,
    rating INT NOT NULL,
    comments VARCHAR(10000),
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (workoutId) REFERENCES workout(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

-- Conteúdo / rede social
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
    authorId BIGINT,
    repost BOOLEAN NOT NULL DEFAULT FALSE,
    repostFromcontentId BIGINT DEFAULT NULL,
    repostByAuthorId BIGINT DEFAULT NULL,
    visible BOOLEAN NOT NULL DEFAULT TRUE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (repostByAuthorId) REFERENCES appUser(id)
);

CREATE TABLE IF NOT EXISTS contentContentCategory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contentId BIGINT NOT NULL,
    categoryId INT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
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

CREATE TABLE IF NOT EXISTS savedContent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    savedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

CREATE TABLE IF NOT EXISTS report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contentId BIGINT NOT NULL,
    reportedByUserId BIGINT NOT NULL,
    reason VARCHAR(10000) NOT NULL,
    valid BOOLEAN NOT NULL DEFAULT TRUE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (reportedByUserId) REFERENCES appUser(id)
);
