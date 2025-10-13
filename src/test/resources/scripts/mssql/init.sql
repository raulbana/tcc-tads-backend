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
    iciq3answer INT NOT NULL,
    iciq4answer INT NOT NULL,
    iciq5answer INT NOT NULL,
    iciqScore INT NOT NULL,
    urinationLoss NVARCHAR(26) NOT NULL,
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

CREATE TABLE role (
    id INT IDENTITY(1,1) PRIMARY KEY,
    description NVARCHAR(50) NOT NULL,
    permissionLevel INT NOT NULL,
    reason NVARCHAR(255) NOT NULL,
    hasDocument BIT NOT NULL DEFAULT 0,
    documentType NVARCHAR(50),
    documentValue NVARCHAR(255),
    conceivedBy BIGINT DEFAULT NULL,
    conceivedAt DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- Tabela principal de usuários
CREATE TABLE appUser (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    profilePictureUrl NVARCHAR(255) DEFAULT NULL,
    credentialId BIGINT NOT NULL,
    patientProfileId BIGINT NOT NULL,
    preferencesId BIGINT NOT NULL,
    roleId INT NOT NULL,
    strikes INT NOT NULL DEFAULT 0,
    blocked BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (credentialId) REFERENCES credential(id),
    FOREIGN KEY (patientProfileId) REFERENCES patientProfile(id),
    FOREIGN KEY (preferencesId) REFERENCES preferences(id),
    FOREIGN KEY (roleId) REFERENCES role(id)
);
GO

ALTER TABLE role
ADD CONSTRAINT FK_ConceivedBy FOREIGN KEY (conceivedBy) REFERENCES appUser(id);
GO

-- Tabela para armazenar códigos OTP para recuperação de senha

CREATE TABLE OTP (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    userId BIGINT NOT NULL,
    code NVARCHAR(255) NOT NULL,
    expirationTime BIGINT NOT NULL,
    used BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (userId) REFERENCES appUser(id)
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
-- Tabela para armazenar mídias (imagens, vídeos, etc.)
CREATE TABLE media (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    url NVARCHAR(255) NOT NULL,
    contentType NVARCHAR(10) NOT NULL,
    contentSize BIGINT NOT NULL,
    altText NVARCHAR(255) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

-- Tabela de exercícios e planos de treino

CREATE TABLE exerciseAttribute (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    type NVARCHAR(50) NOT NULL CHECK (type IN ('BENEFIT', 'CONTRAINDICATION')),
    description NVARCHAR(255) DEFAULT NULL
);
GO

CREATE TABLE exerciseCategory (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(255) DEFAULT NULL
);
GO

CREATE TABLE exercise (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    categoryId INT NOT NULL,
    instructions NVARCHAR(MAX) NOT NULL,
    repetitions INT NOT NULL,
    sets INT NOT NULL,
    restTime INT NOT NULL,
    duration INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (categoryId) REFERENCES exerciseCategory(id)
);
GO

CREATE TABLE exerciseExerciseAttribute (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    exerciseId BIGINT NOT NULL,
    attributeId INT NOT NULL,
    FOREIGN KEY (exerciseId) REFERENCES exercise(id),
    FOREIGN KEY (attributeId) REFERENCES exerciseAttribute(id)
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

CREATE TABLE workout (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
    totalDuration INT NOT NULL,
    difficultyLevel NVARCHAR(50) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE workoutExercise (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    workoutId BIGINT NOT NULL,
    exerciseId BIGINT NOT NULL,
    exerciseOrder INT NOT NULL,
    FOREIGN KEY (workoutId) REFERENCES workout(id),
    FOREIGN KEY (exerciseId) REFERENCES exercise(id)
);
GO

CREATE TABLE workoutPlan (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    description NVARCHAR(MAX) DEFAULT NULL,
    daysPerWeek INT NOT NULL,
    totalWeeks INT NOT NULL,
    iciqScoreRecommendation INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE workoutPlanWorkout (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    workoutPlanId BIGINT NOT NULL,
    workoutId BIGINT NOT NULL,
    workoutOrder INT NOT NULL,
    FOREIGN KEY (workoutPlanId) REFERENCES workoutPlan(id),
    FOREIGN KEY (workoutId) REFERENCES workout(id)
);
GO

CREATE TABLE userWorkoutPlan (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    userId BIGINT NOT NULL,
    workoutPlanId BIGINT NOT NULL,
    startDate DATETIME2 NOT NULL DEFAULT GETDATE(),
    endDate DATETIME2 DEFAULT NULL,
    progress INT DEFAULT NULL,
    currentWeek INT NOT NULL DEFAULT 1,
    nextWorkout INT DEFAULT NULL,
    lastWorkoutDate DATETIME2 DEFAULT NULL,
    completed BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    UNIQUE (userId, workoutPlanId),
    FOREIGN KEY (userId) REFERENCES appUser(id),
    FOREIGN KEY (workoutPlanId) REFERENCES workoutPlan(id)
);
GO

CREATE TABLE workoutFeedback (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    workoutId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    evaluation NVARCHAR(20) NOT NULL,
    rating INT NOT NULL,
    comments NVARCHAR(MAX),
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (workoutId) REFERENCES workout(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

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
    visible BIT NOT NULL DEFAULT 1,
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

CREATE TABLE savedContent (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    savedAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (userId) REFERENCES appUser(id)
);
GO

CREATE TABLE report (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    contentId BIGINT NOT NULL,
    reportedByUserId BIGINT NOT NULL,
    reason NVARCHAR(MAX) NOT NULL,
    valid BIT NOT NULL DEFAULT 1,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (contentId) REFERENCES content(id),
    FOREIGN KEY (reportedByUserId) REFERENCES appUser(id)
);
GO