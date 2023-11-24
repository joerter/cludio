CREATE TABLE IF NOT EXISTS
  public.season (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    date_range daterange NOT NULL
  );

ALTER TABLE
  public.season
ADD
  CONSTRAINT season_pkey PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS
  public.teacher (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    title character varying(255) NULL
  );

ALTER TABLE
  public.teacher
ADD
  CONSTRAINT teacher_pkey PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS
  public.student (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL
  );

ALTER TABLE
  public.student
ADD
  CONSTRAINT student_pkey PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS
  public.class (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    name character varying(255) NOT NULL,
    season_id integer NOT NULL
  );

ALTER TABLE
  public.class
ADD
  CONSTRAINT class_pkey PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS
  public.class_repetition (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    day_of_the_week_id integer NOT NULL,
    teacher_id integer NOT NULL,
    class_id integer NOT NULL,
    "time" character varying(255) NOT NULL
  );

ALTER TABLE
  public.class_repetition
ADD
  CONSTRAINT class_repetition_pkey PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS
  public.class_schedule (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    class_id integer NOT NULL,
    class_repetition_id integer NOT NULL,
    datetime timestamp without time zone NOT NULL
  );

ALTER TABLE
  public.class_schedule
ADD
  CONSTRAINT class_schedule_pkey PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS
  public.enrollments (
    id serial NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    student_id integer NOT NULL,
    class_id integer NOT NULL
  );

ALTER TABLE
  public.enrollments
ADD
  CONSTRAINT enrollments_pkey PRIMARY KEY (id);
