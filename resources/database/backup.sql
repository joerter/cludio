--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4 (Debian 15.4-2.pgdg120+1)
-- Dumped by pg_dump version 15.4 (Debian 15.4-2.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: class; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.class (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.class OWNER TO cludio;

--
-- Name: class_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.class_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.class_id_seq OWNER TO cludio;

--
-- Name: class_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.class_id_seq OWNED BY public.class.id;


--
-- Name: class_repetition; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.class_repetition (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    day_of_the_week_id integer NOT NULL,
    teacher_id integer NOT NULL,
    class_id integer NOT NULL,
    "time" time without time zone
);


ALTER TABLE public.class_repetition OWNER TO cludio;

--
-- Name: class_repetition_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.class_repetition_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.class_repetition_id_seq OWNER TO cludio;

--
-- Name: class_repetition_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.class_repetition_id_seq OWNED BY public.class_repetition.id;


--
-- Name: class_schedule; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.class_schedule (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    class_id integer NOT NULL,
    class_repetition_id integer NOT NULL,
    datetime timestamp without time zone NOT NULL
);


ALTER TABLE public.class_schedule OWNER TO cludio;

--
-- Name: class_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.class_schedule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.class_schedule_id_seq OWNER TO cludio;

--
-- Name: class_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.class_schedule_id_seq OWNED BY public.class_schedule.id;


--
-- Name: day_of_the_week; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.day_of_the_week (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    day character varying(255) NOT NULL
);


ALTER TABLE public.day_of_the_week OWNER TO cludio;

--
-- Name: day_of_the_week_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.day_of_the_week_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.day_of_the_week_id_seq OWNER TO cludio;

--
-- Name: day_of_the_week_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.day_of_the_week_id_seq OWNED BY public.day_of_the_week.id;


--
-- Name: enrollments; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.enrollments (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    student_id integer NOT NULL,
    class_id integer NOT NULL
);


ALTER TABLE public.enrollments OWNER TO cludio;

--
-- Name: enrollments_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.enrollments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.enrollments_id_seq OWNER TO cludio;

--
-- Name: enrollments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.enrollments_id_seq OWNED BY public.enrollments.id;


--
-- Name: schema_version; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.schema_version (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.schema_version OWNER TO cludio;

--
-- Name: student; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.student (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL
);


ALTER TABLE public.student OWNER TO cludio;

--
-- Name: student_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.student_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.student_id_seq OWNER TO cludio;

--
-- Name: student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.student_id_seq OWNED BY public.student.id;


--
-- Name: teacher; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.teacher (
    id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    title character varying(255)
);


ALTER TABLE public.teacher OWNER TO cludio;

--
-- Name: teacher_id_seq; Type: SEQUENCE; Schema: public; Owner: cludio
--

CREATE SEQUENCE public.teacher_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teacher_id_seq OWNER TO cludio;

--
-- Name: teacher_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cludio
--

ALTER SEQUENCE public.teacher_id_seq OWNED BY public.teacher.id;


--
-- Name: todo; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.todo (
    todo_id uuid DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    title text NOT NULL
);


ALTER TABLE public.todo OWNER TO cludio;

--
-- Name: todo_item; Type: TABLE; Schema: public; Owner: cludio
--

CREATE TABLE public.todo_item (
    todo_item_id uuid DEFAULT gen_random_uuid() NOT NULL,
    todo_id uuid,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    title text NOT NULL
);


ALTER TABLE public.todo_item OWNER TO cludio;

--
-- Name: class id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class ALTER COLUMN id SET DEFAULT nextval('public.class_id_seq'::regclass);


--
-- Name: class_repetition id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_repetition ALTER COLUMN id SET DEFAULT nextval('public.class_repetition_id_seq'::regclass);


--
-- Name: class_schedule id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_schedule ALTER COLUMN id SET DEFAULT nextval('public.class_schedule_id_seq'::regclass);


--
-- Name: day_of_the_week id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.day_of_the_week ALTER COLUMN id SET DEFAULT nextval('public.day_of_the_week_id_seq'::regclass);


--
-- Name: enrollments id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.enrollments ALTER COLUMN id SET DEFAULT nextval('public.enrollments_id_seq'::regclass);


--
-- Name: student id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.student ALTER COLUMN id SET DEFAULT nextval('public.student_id_seq'::regclass);


--
-- Name: teacher id; Type: DEFAULT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.teacher ALTER COLUMN id SET DEFAULT nextval('public.teacher_id_seq'::regclass);


--
-- Data for Name: class; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.class (id, created_at, name) FROM stdin;
2	2023-10-30 12:49:48.266102	Ballet I
1	2023-10-30 12:49:17.252294	Ballet II
3	2023-11-06 00:57:44.418651	Ballet III
4	2023-11-06 01:28:38.414237	Creative Movement
5	2023-11-06 01:28:38.414237	Jazz I
6	2023-11-06 01:28:38.414237	Jazz II
7	2023-11-06 13:30:14.647215	Acro and Tumbling
8	2023-11-06 13:30:14.647215	Hip Hop
\.


--
-- Data for Name: class_repetition; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.class_repetition (id, created_at, day_of_the_week_id, teacher_id, class_id, "time") FROM stdin;
\.


--
-- Data for Name: class_schedule; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.class_schedule (id, created_at, class_id, class_repetition_id, datetime) FROM stdin;
\.


--
-- Data for Name: day_of_the_week; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.day_of_the_week (id, created_at, day) FROM stdin;
1	2023-10-30 12:13:52.693109	Monday
2	2023-10-30 12:13:52.693109	Tuesday
3	2023-10-30 12:13:52.693109	Wednesday
4	2023-10-30 12:13:52.693109	Thursday
5	2023-10-30 12:13:52.693109	Friday
6	2023-10-30 12:13:52.693109	Saturday
7	2023-10-30 12:13:52.693109	Sunday
\.


--
-- Data for Name: enrollments; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.enrollments (id, created_at, student_id, class_id) FROM stdin;
\.


--
-- Data for Name: schema_version; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	add todo tables	SQL	V1__add_todo_tables.sql	-1600373265	cludio	2023-10-16 06:45:46.133794	17	t
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.student (id, created_at, first_name, last_name) FROM stdin;
1	2023-10-30 12:36:45.263024	John	Oerter
2	2023-10-30 12:36:45.263024	Olivia	Martinez
3	2023-10-30 12:36:45.263024	Noah	Thompson
4	2023-10-30 12:36:45.263024	Ava	Robinson
5	2023-10-30 12:36:45.263024	Jackson	Mitchell
6	2023-10-30 12:36:45.263024	Sophia	Patterson
7	2023-10-30 12:36:45.263024	Liam	Hernandez
8	2023-10-30 12:36:45.263024	Mia	Henderson
9	2023-10-30 12:36:45.263024	Lucas	Rivera
10	2023-10-30 12:36:45.263024	Emma	Gonzalez
11	2023-10-30 12:36:45.263024	Benjamin	Brooks
12	2023-10-30 12:36:45.263024	Amelia	Griffin
13	2023-10-30 12:36:45.263024	Elijah	Black
14	2023-10-30 12:36:45.263024	Chloe	Murphy
15	2023-10-30 12:36:45.263024	Aiden	Palmer
16	2023-10-30 12:36:45.263024	Lily	Cole
17	2023-10-30 12:36:45.263024	Daniel	Stone
18	2023-10-30 12:36:45.263024	Zoe	Warren
19	2023-10-30 12:36:45.263024	Samuel	Chambers
20	2023-10-30 12:36:45.263024	Bella	McKenzie
21	2023-10-30 12:36:45.263024	Ethan	Sharp
23	2023-11-06 13:41:59.26732	John	Doe
24	2023-11-06 13:41:59.26732	Alice	Smith
25	2023-11-06 13:41:59.26732	Michael	Johnson
26	2023-11-06 13:41:59.26732	Emily	Williams
27	2023-11-06 13:41:59.26732	James	Brown
28	2023-11-06 13:41:59.26732	Olivia	Davis
29	2023-11-06 13:41:59.26732	William	Wilson
30	2023-11-06 13:41:59.26732	Ava	Martinez
31	2023-11-06 13:41:59.26732	Benjamin	Garcia
32	2023-11-06 13:41:59.26732	Sophia	Rodriguez
33	2023-11-06 13:41:59.26732	Daniel	Lopez
34	2023-11-06 13:41:59.26732	Emma	Taylor
35	2023-11-06 13:41:59.26732	Liam	Miller
36	2023-11-06 13:41:59.26732	Mia	Anderson
37	2023-11-06 13:41:59.26732	Elijah	Hernandez
38	2023-11-06 13:41:59.26732	Charlotte	Moore
39	2023-11-06 13:41:59.26732	Logan	Jackson
40	2023-11-06 13:41:59.26732	Ella	White
41	2023-11-06 13:41:59.26732	Alexander	Martin
42	2023-11-06 13:41:59.26732	Grace	Thompson
\.


--
-- Data for Name: teacher; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.teacher (id, created_at, first_name, last_name, title) FROM stdin;
1	2023-10-30 12:33:38.775413	Rachel	Smith	Ms.
2	2023-10-30 12:35:14.438196	Elena	Carter	Ms.
3	2023-10-30 12:35:14.438196	Wyatt	Payne	Mr.
4	2023-10-30 12:35:14.438196	Cameron	Miller	Mr.
5	2023-10-30 12:35:14.438196	Katerina	Miller	Ms.
\.


--
-- Data for Name: todo; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.todo (todo_id, created_at, title) FROM stdin;
1e9725a2-e3aa-47d6-b085-19bad9a77ae2	2023-10-18 11:59:38.858476	Test todo
\.


--
-- Data for Name: todo_item; Type: TABLE DATA; Schema: public; Owner: cludio
--

COPY public.todo_item (todo_item_id, todo_id, created_at, title) FROM stdin;
\.


--
-- Name: class_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.class_id_seq', 8, true);


--
-- Name: class_repetition_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.class_repetition_id_seq', 1, false);


--
-- Name: class_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.class_schedule_id_seq', 1, false);


--
-- Name: day_of_the_week_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.day_of_the_week_id_seq', 7, true);


--
-- Name: enrollments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.enrollments_id_seq', 1, false);


--
-- Name: student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.student_id_seq', 42, true);


--
-- Name: teacher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cludio
--

SELECT pg_catalog.setval('public.teacher_id_seq', 5, true);


--
-- Name: class class_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class
    ADD CONSTRAINT class_pkey PRIMARY KEY (id);


--
-- Name: class_repetition class_repetition_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_repetition
    ADD CONSTRAINT class_repetition_pkey PRIMARY KEY (id);


--
-- Name: class_schedule class_schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_schedule
    ADD CONSTRAINT class_schedule_pkey PRIMARY KEY (id);


--
-- Name: day_of_the_week day_of_the_week_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.day_of_the_week
    ADD CONSTRAINT day_of_the_week_pkey PRIMARY KEY (id);


--
-- Name: enrollments enrollments_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT enrollments_pkey PRIMARY KEY (id);


--
-- Name: schema_version schema_version_pk; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (installed_rank);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (id);


--
-- Name: teacher teacher_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.teacher
    ADD CONSTRAINT teacher_pkey PRIMARY KEY (id);


--
-- Name: todo_item todo_item_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.todo_item
    ADD CONSTRAINT todo_item_pkey PRIMARY KEY (todo_item_id);


--
-- Name: todo todo_pkey; Type: CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.todo
    ADD CONSTRAINT todo_pkey PRIMARY KEY (todo_id);


--
-- Name: schema_version_s_idx; Type: INDEX; Schema: public; Owner: cludio
--

CREATE INDEX schema_version_s_idx ON public.schema_version USING btree (success);


--
-- Name: class_repetition fk_class_repetition_class_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_repetition
    ADD CONSTRAINT fk_class_repetition_class_id FOREIGN KEY (class_id) REFERENCES public.class(id);


--
-- Name: class_repetition fk_class_repetition_day_of_the_week_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_repetition
    ADD CONSTRAINT fk_class_repetition_day_of_the_week_id FOREIGN KEY (day_of_the_week_id) REFERENCES public.day_of_the_week(id);


--
-- Name: class_repetition fk_class_repetition_teacher_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_repetition
    ADD CONSTRAINT fk_class_repetition_teacher_id FOREIGN KEY (teacher_id) REFERENCES public.teacher(id);


--
-- Name: class_schedule fk_class_schedule_class_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_schedule
    ADD CONSTRAINT fk_class_schedule_class_id FOREIGN KEY (class_id) REFERENCES public.class(id);


--
-- Name: class_schedule fk_class_schedule_class_repetition_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.class_schedule
    ADD CONSTRAINT fk_class_schedule_class_repetition_id FOREIGN KEY (class_repetition_id) REFERENCES public.class_repetition(id);


--
-- Name: enrollments fk_enrollments_class_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT fk_enrollments_class_id FOREIGN KEY (class_id) REFERENCES public.class(id);


--
-- Name: enrollments fk_enrollments_student_id; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT fk_enrollments_student_id FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: todo_item todo_item_todo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: cludio
--

ALTER TABLE ONLY public.todo_item
    ADD CONSTRAINT todo_item_todo_id_fkey FOREIGN KEY (todo_id) REFERENCES public.todo(todo_id);


--
-- PostgreSQL database dump complete
--

