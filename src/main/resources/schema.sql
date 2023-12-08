DROP TABLE IF EXISTS
tasks
CASCADE;

CREATE TABLE IF NOT EXISTS PUBLIC.tasks (
id INTEGER,
title VARCHAR(255) NOT NULL,
description VARCHAR(255) NOT NULL,
startDate DATE,
dueDate DATE,
completed BOOLEAN,
CONSTRAINT pk_tasks_id PRIMARY KEY (id)
);