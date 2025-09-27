CREATE DATABASE dailyiu_backend_storage_database;
GO

USE dailyiu_backend_storage_database;
GO

CREATE TABLE credential (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    passwordHash NVARCHAR(255) NOT NULL,
    salt NVARCHAR(255) NOT NULL
);
GO

CREATE TABLE patientProfile (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    birthDate DATE NOT NULL,
    gender NVARCHAR(10) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE preferences (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    highContrast BIT NOT NULL DEFAULT 0,
    bigFont BIT NOT NULL DEFAULT 0,
    reminderCalendar BIT NOT NULL DEFAULT 0,
    reminderCalendarSchedule TIME,
    reminderWorkout BIT NOT NULL DEFAULT 0,
    reminderWorkoutSchedule TIME,
    encouragingMessages BIT NOT NULL DEFAULT 1,
    workoutMediaType NVARCHAR(10) NOT NULL DEFAULT 'VIDEO'
);
GO

-- Tabela principal de usuários
CREATE TABLE appUser (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    profilePictureUrl NVARCHAR(255) DEFAULT NULL,
    credentialId BIGINT NOT NULL,
    patientProfileId BIGINT NOT NULL,
    preferencesId BIGINT NOT NULL,
    FOREIGN KEY (credentialId) REFERENCES credential(id),
    FOREIGN KEY (patientProfileId) REFERENCES patientProfile(id),
    FOREIGN KEY (preferencesId) REFERENCES preferences(id)
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Tabela principal de questões com campos adicionais
CREATE TABLE question (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    externalId NVARCHAR(50),
    text NVARCHAR(MAX) NOT NULL,
    type NVARCHAR(10) NOT NULL,
    required BIT NOT NULL DEFAULT 0,
    placeholder NVARCHAR(255),
    minValue INT,
    maxValue INT,
    step INT
);
GO

-- Tabela para armazenar as opções das questões (para RADIO e CHECKBOX)
CREATE TABLE questionOption (
    id INT IDENTITY(1,1) PRIMARY KEY,
    questionId BIGINT,
    label NVARCHAR(255) NOT NULL,
    textValue NVARCHAR(50) NOT NULL,
    FOREIGN KEY (questionId) REFERENCES question(id)
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Tabela para armazenar registros para o diário miccional
CREATE TABLE calendarDay (
    id INT IDENTITY(1,1) PRIMARY KEY,
    dateValue DATE NOT NULL,
    userId BIGINT NOT NULL,
    leakageLevel NVARCHAR(20) NOT NULL,
    eventsCount INT NOT NULL DEFAULT 0,
    completedExercises INT NOT NULL DEFAULT 0,
    notesPreview NVARCHAR(MAX),
    dayTitle NVARCHAR(10) NOT NULL,
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE urinationData (
    id INT IDENTITY(1,1) PRIMARY KEY,
    calendarDayId INT NOT NULL,
    timeValue TIME NOT NULL,
    amount NVARCHAR(20) NOT NULL,
    leakage BIT NOT NULL DEFAULT 0,
    reason NVARCHAR(MAX),
    urgency BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (calendarDayId) REFERENCES calendarDay(id)
);
GO

--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//

-- Tabela de exercícios e conteúdos de treino

CREATE TABlE media (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    url NVARCHAR(255) NOT NULL,
    contentType NVARCHAR(10) NOT NULL,
    contentSize BIGINT NOT NULL,
    altText NVARCHAR(255) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE exercise (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    category INT NOT NULL,
    intensityLevel INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE exerciseMedia (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);
GO

CREATE TABLE exerciseFeedback (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);

-- Tabela de conteúdos com funcionalidades de rede social

CREATE TABLE contentCategory (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(255),
    auditable BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE content (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
    subtitle NVARCHAR(255),
    subContent NVARCHAR(MAX),
    categoryId INT NOT NULL,
    authorId BIGINT,
    repost BIT NOT NULL DEFAULT 0,
    repostFromcontentId BIGINT DEFAULT NULL,
    repostByAuthorId BIGINT DEFAULT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (repostByAuthorId) REFERENCES appUser(id),
    FOREIGN KEY (categoryId) REFERENCES contentCategory(id)
);
GO

CREATE TABLE comment (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    authorId BIGINT NOT NULL,
    text NVARCHAR(MAX) NOT NULL,
    reply BIT NOT NULL DEFAULT 0,
    replyToCommentId BIGINT DEFAULT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (authorId) REFERENCES appUser(id),
    FOREIGN KEY (replyToCommentId) REFERENCES comment(id)
);
GO

CREATE TABLE contentLikes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE commentLikes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    commentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (commentId) REFERENCES comment(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE contentMedia (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    mediaId BIGINT NOT NULL,
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (mediaId) REFERENCES media(id)
);
GO