--
-- PostgreSQL database dump
--

\restrict t6sQcXv0cB2vww0Vvip9xjZzT7wcW0shL1vCWtHjuW9aTJGYretUZxdCZIZFpvU

-- Dumped from database version 16.13 (Debian 16.13-1.pgdg13+1)
-- Dumped by pg_dump version 16.13 (Debian 16.13-1.pgdg13+1)

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
-- Name: bookings; Type: TABLE; Schema: public; Owner: uniride_user
--

CREATE TABLE public.bookings (
    id bigint NOT NULL,
    trip_id bigint NOT NULL,
    passenger_id bigint NOT NULL,
    status character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    review boolean
);


ALTER TABLE public.bookings OWNER TO uniride_user;

--
-- Name: bookings_id_seq; Type: SEQUENCE; Schema: public; Owner: uniride_user
--

CREATE SEQUENCE public.bookings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bookings_id_seq OWNER TO uniride_user;

--
-- Name: bookings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: uniride_user
--

ALTER SEQUENCE public.bookings_id_seq OWNED BY public.bookings.id;


--
-- Name: reviews; Type: TABLE; Schema: public; Owner: uniride_user
--

CREATE TABLE public.reviews (
    id bigint NOT NULL,
    reviewer_id bigint,
    reviewed_id bigint,
    rating integer,
    comment text,
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT reviews_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


ALTER TABLE public.reviews OWNER TO uniride_user;

--
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: uniride_user
--

CREATE SEQUENCE public.reviews_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reviews_id_seq OWNER TO uniride_user;

--
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: uniride_user
--

ALTER SEQUENCE public.reviews_id_seq OWNED BY public.reviews.id;


--
-- Name: trips; Type: TABLE; Schema: public; Owner: uniride_user
--

CREATE TABLE public.trips (
    id bigint NOT NULL,
    driver_id bigint,
    origin character varying(255) NOT NULL,
    destination character varying(255) NOT NULL,
    departure timestamp without time zone NOT NULL,
    seats integer NOT NULL,
    price double precision NOT NULL,
    only_women boolean DEFAULT false NOT NULL,
    has_ac boolean DEFAULT false NOT NULL,
    status character varying(255) DEFAULT 'ACTIVE'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    estado character varying(255) NOT NULL
);


ALTER TABLE public.trips OWNER TO uniride_user;

--
-- Name: trips_id_seq; Type: SEQUENCE; Schema: public; Owner: uniride_user
--

CREATE SEQUENCE public.trips_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trips_id_seq OWNER TO uniride_user;

--
-- Name: trips_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: uniride_user
--

ALTER SEQUENCE public.trips_id_seq OWNED BY public.trips.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: uniride_user
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    full_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password_hash character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    rol character varying(20) NOT NULL,
    vehicle_plate character varying(10),
    vehicle_color character varying(30),
    rating double precision DEFAULT 5.0 NOT NULL,
    total_ratings integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.users OWNER TO uniride_user;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: uniride_user
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO uniride_user;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: uniride_user
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: bookings id; Type: DEFAULT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.bookings ALTER COLUMN id SET DEFAULT nextval('public.bookings_id_seq'::regclass);


--
-- Name: reviews id; Type: DEFAULT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.reviews ALTER COLUMN id SET DEFAULT nextval('public.reviews_id_seq'::regclass);


--
-- Name: trips id; Type: DEFAULT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.trips ALTER COLUMN id SET DEFAULT nextval('public.trips_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: uniride_user
--

COPY public.bookings (id, trip_id, passenger_id, status, created_at, review) FROM stdin;
5	1	1	COMPLETED	2026-04-30 04:23:35.95899	f
3	1	3	COMPLETED	2026-04-30 04:07:57.058317	f
2	1	3	COMPLETED	2026-04-30 04:07:50.484439	f
1	1	3	COMPLETED	2026-04-30 04:07:42.64565	f
8	2	2	COMPLETED	2026-04-30 04:30:57.390209	f
6	2	1	COMPLETED	2026-04-30 04:23:35.960982	f
4	2	3	COMPLETED	2026-04-30 04:18:33.925744	f
10	4	2	COMPLETED	2026-04-30 06:14:10.62308	f
11	3	2	COMPLETED	2026-04-30 06:20:58.148327	f
9	3	3	COMPLETED	2026-04-30 05:19:10.741596	f
12	6	2	COMPLETED	2026-04-30 10:25:40.360683	f
16	12	7	COMPLETED	2026-05-01 21:40:19.554045	f
15	12	7	COMPLETED	2026-05-01 21:36:20.616574	f
13	12	8	COMPLETED	2026-05-01 18:45:22.413925	f
14	17	8	COMPLETED	2026-05-01 18:50:09.633966	f
17	21	7	COMPLETED	2026-05-01 22:25:06.700813	f
18	22	7	COMPLETED	2026-05-01 22:25:06.700813	f
35	51	5	COMPLETED	2026-05-04 11:48:13.762264	f
34	51	5	COMPLETED	2026-05-04 11:48:10.775547	f
19	39	7	COMPLETED	2026-05-03 00:12:33.753715	f
21	40	2	COMPLETED	2026-05-03 00:18:02.5042	f
25	44	7	COMPLETED	2026-05-03 01:28:36.473011	f
22	42	7	COMPLETED	2026-05-03 01:16:55.549548	f
30	48	3	COMPLETED	2026-05-03 03:04:48.147882	f
24	43	2	COMPLETED	2026-05-03 01:19:32.331815	f
23	43	2	COMPLETED	2026-05-03 01:19:15.843028	f
37	54	5	CONFIRMED	2026-05-04 22:50:33.737243	f
38	54	5	CONFIRMED	2026-05-04 22:51:18.460866	f
33	50	5	COMPLETED	2026-05-04 11:22:21.331994	f
32	50	5	COMPLETED	2026-05-04 11:17:25.026739	f
45	60	5	COMPLETED	2026-05-05 08:22:48.514331	\N
40	56	5	COMPLETED	2026-05-04 23:43:39.407369	\N
46	61	5	CONFIRMED	2026-05-06 19:13:15.632278	\N
7	3	1	COMPLETED	2026-04-30 04:23:35.96181	f
47	61	5	CONFIRMED	2026-05-06 19:13:20.415578	\N
48	62	5	CONFIRMED	2026-05-06 19:46:01.654658	\N
49	63	5	CONFIRMED	2026-05-06 19:52:26.943614	\N
50	64	5	REJECTED	2026-05-06 19:56:36.628234	\N
51	65	5	CONFIRMED	2026-05-06 19:59:57.185888	\N
39	55	5	COMPLETED	2026-05-04 23:10:45.856858	f
31	49	2	COMPLETED	2026-05-03 03:11:10.704627	f
28	36	7	COMPLETED	2026-05-03 02:12:51.468976	f
27	36	7	COMPLETED	2026-05-03 02:00:59.506625	f
26	36	7	COMPLETED	2026-05-03 01:39:46.158924	f
20	36	7	COMPLETED	2026-05-03 00:12:38.753321	f
29	47	10	COMPLETED	2026-05-03 03:01:47.436845	f
36	52	5	COMPLETED	2026-05-04 11:59:29.580011	f
41	57	5	COMPLETED	2026-05-04 23:46:43.640293	\N
42	56	5	CONFIRMED	2026-05-05 00:09:25.06773	\N
43	58	2	COMPLETED	2026-05-05 00:13:06.291958	\N
44	59	5	COMPLETED	2026-05-05 08:05:14.106872	\N
\.


--
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: uniride_user
--

COPY public.reviews (id, reviewer_id, reviewed_id, rating, comment, created_at) FROM stdin;
1	7	2	5	Excelente viaje	2026-05-03 02:01:26.74527
2	7	2	4	Excelente conductor 	2026-05-03 02:07:01.824034
3	7	2	5	Excelenteeeeeeeee	2026-05-03 02:07:17.21905
4	2	7	4	Genial	2026-05-03 02:07:45.512144
5	2	7	5	kdk	2026-05-03 02:09:38.982762
6	7	2	1	zunga	2026-05-03 02:23:23.150628
7	7	2	1	jejejejeje	2026-05-03 02:23:56.83718
8	3	2	5	esta muy rico el conducto, ojalá se repita	2026-05-03 03:06:00.448026
9	5	2	2	Pesimo servicio, demandelo por acoso!!!!!!	2026-05-04 11:32:40.711161
10	5	2	5	Genial	2026-05-04 11:38:22.562145
11	5	2	5	Sapo me pregunto sobre mi novio	2026-05-04 11:53:42.311905
12	5	2	4	Te oido	2026-05-04 12:07:40.577216
13	5	2	1	zunga	2026-05-04 12:09:52.791617
14	5	2	2	,,,,,,	2026-05-04 12:11:28.701168
15	5	2	3	nukibyb	2026-05-04 12:13:03.760177
16	5	2	1	366	2026-05-04 12:15:58.472941
17	5	2	4	Hola	2026-05-04 21:59:47.082438
18	5	2	2	yes	2026-05-05 00:02:24.794117
19	5	2	3	Hola 123	2026-05-05 08:34:09.567884
20	5	2	3	Hellor	2026-05-06 15:45:08.97192
\.


--
-- Data for Name: trips; Type: TABLE DATA; Schema: public; Owner: uniride_user
--

COPY public.trips (id, driver_id, origin, destination, departure, seats, price, only_women, has_ac, status, created_at, estado) FROM stdin;
24	2	Universidad Javeriana	Centro	2026-05-01 22:55:00	3	5000	f	f	ACTIVE	2026-05-01 22:50:48.828325	COMPLETED
26	2	Universidad Javeriana	Centro	2026-05-01 23:00:00	3	5000	f	f	ACTIVE	2026-05-01 22:56:27.772784	COMPLETED
27	2	lozano	Suba	2026-05-01 18:06:00	4	4900	t	f	ACTIVE	2026-05-01 23:03:59.993535	COMPLETED
28	2	lozano	Suba	2026-05-01 18:21:00	4	5000	t	f	ACTIVE	2026-05-01 23:06:40.512185	COMPLETED
29	2	lozano	Suba	2026-05-01 20:00:00	3	8000	f	f	ACTIVE	2026-05-01 23:12:07.376363	COMPLETED
31	2	lozano	Suba	2026-05-01 19:04:00	4	70000	f	f	ACTIVE	2026-05-01 23:16:29.779998	COMPLETED
37	2	Javeriana	Chapinero	2026-05-02 19:51:00	3	3000	t	f	ACTIVE	2026-05-02 23:51:04.033831	COMPLETED
32	2	lozano	Suba	2026-05-01 19:19:00	4	4000	t	f	ACTIVE	2026-05-01 23:19:34.790813	COMPLETED
38	2	Javeriana	Chapinero	2026-05-02 19:09:00	2	3000	t	f	ACTIVE	2026-05-03 00:05:46.973274	COMPLETED
1	1	Universidad Javeriana	Centro Chía	2026-04-30 06:05:28.872161	0	4000	f	f	ACTIVE	2026-04-30 04:05:28.872161	COMPLETED
2	1	Universidad Javeriana	Portal Norte	2026-04-30 07:05:28.876058	0	5500	t	f	ACTIVE	2026-04-30 04:05:28.876058	COMPLETED
5	2	Javeriana	Castilla	2026-04-30 03:58:00	4	8000	f	f	ACTIVE	2026-04-30 05:55:44.044854	COMPLETED
4	1	Universidad Nacional	Calle 72	2026-04-30 09:05:28.877685	1	3000	f	f	ACTIVE	2026-04-30 04:05:28.877685	COMPLETED
3	1	Universidad Javeriana	Suba	2026-04-30 08:05:28.876799	2	6000	f	f	ACTIVE	2026-04-30 04:05:28.876799	COMPLETED
7	2	Javeriana	Castilla	2026-04-30 01:23:00	3	2000	t	f	ACTIVE	2026-04-30 06:21:26.480218	COMPLETED
8	2	Javeriana	Castilla	2026-04-29 20:28:00	4	3000	t	f	ACTIVE	2026-04-30 01:26:23.16009	COMPLETED
9	2	Javeriana	Santa Barbara	2026-04-29 20:37:00	4	4000	f	f	ACTIVE	2026-04-30 01:33:18.970492	COMPLETED
10	2	Javeriana	Santa Barbara	2026-04-30 06:40:00	3	1234	f	f	ACTIVE	2026-04-30 01:35:39.474972	COMPLETED
11	2	Javeriana	Silvania	2026-05-01 09:41:00	3	3333	f	f	ACTIVE	2026-04-30 01:38:17.209168	COMPLETED
13	2	Javeriana	Oasis	2026-05-01 05:00:00	3	5677	f	f	ACTIVE	2026-04-30 01:44:45.215542	COMPLETED
14	2	Universidad Javeriana	Centro Chía	2026-04-30 01:50:00	3	5000	f	f	ACTIVE	2026-04-30 01:46:29.387968	COMPLETED
15	2	Universidad Javeriana	Centro Chía	2026-04-30 02:51:12.63772	4	5000	f	f	ACTIVE	2026-04-30 01:51:12.63772	COMPLETED
6	3	Javeriana	Castilla	2026-04-30 17:57:00	2	7000	t	f	ACTIVE	2026-04-30 05:57:43.052166	COMPLETED
16	2	Javeriana	Oasis	2026-05-01 00:28:00	4	5000	f	f	ACTIVE	2026-04-30 10:26:49.801741	COMPLETED
12	2	Nacional	Madelena	2026-05-01 23:00:00	0	7777	f	f	ACTIVE	2026-04-30 01:43:08.402407	COMPLETED
30	2	lozano	Suba	2026-05-02 06:14:00	4	50000	f	f	ACTIVE	2026-05-01 23:14:20.897656	COMPLETED
17	2	Javeriana	Oasis	2026-05-02 01:52:00	2	20000	f	f	ACTIVE	2026-05-01 18:48:53.66678	COMPLETED
18	2	Javeriana	Oasis	2026-05-02 21:30:00	3	2000	t	f	ACTIVE	2026-05-01 21:21:15.439491	COMPLETED
19	2	Javeriana	Castilla	2026-05-02 16:40:00	4	3000	t	f	ACTIVE	2026-05-01 21:23:49.846626	COMPLETED
20	2	central	Suba	2026-05-02 16:55:00	3	4500	t	f	ACTIVE	2026-05-01 21:31:24.312441	COMPLETED
21	2	central	Suba	2026-05-02 17:20:00	3	3000	f	f	ACTIVE	2026-05-01 22:14:26.345772	COMPLETED
22	2	central	Suba	2026-05-02 17:25:00	4	3000	f	f	ACTIVE	2026-05-01 22:21:54.458541	COMPLETED
23	2	central	Suba	2026-05-02 17:48:00	4	3000	f	f	ACTIVE	2026-05-01 22:46:52.23919	COMPLETED
25	2	lozano	Suba	2026-05-02 18:00:00	3	4000	f	f	ACTIVE	2026-05-01 22:55:28.888806	COMPLETED
33	2	lozano	Suba	2026-05-02 05:23:00	3	40000	t	f	ACTIVE	2026-05-01 23:24:09.962315	COMPLETED
34	2	lozano	Suba	2026-05-02 09:25:00	4	3000	t	f	ACTIVE	2026-05-01 23:25:27.344936	COMPLETED
35	2	lozano	Suba	2026-05-01 23:30:00	2	3000000000	f	f	ACTIVE	2026-05-01 23:27:02.738047	COMPLETED
51	2	Nacional	Suba	2026-05-04 06:50:00	1	30000000	t	f	ACTIVE	2026-05-04 11:47:54.636475	COMPLETED
39	2	Javeriana	Chapinero	2026-05-02 19:27:00	2	3000	t	f	ACTIVE	2026-05-03 00:11:16.826266	COMPLETED
40	7	Javeriana	Chapinero	2026-05-02 19:30:00	3	6000	t	f	ACTIVE	2026-05-03 00:15:50.8538	COMPLETED
44	2	Javeriana	Chapinero	2026-05-02 20:41:00	3	2500	t	f	ACTIVE	2026-05-03 01:28:12.639137	COMPLETED
41	7	Javeriana	Chapinero	2026-05-02 20:32:00	4	6000	t	f	ACTIVE	2026-05-03 01:10:56.636604	COMPLETED
42	2	Javeriana	Chapinero	2026-05-02 20:19:00	2	2900	t	f	ACTIVE	2026-05-03 01:16:26.789596	COMPLETED
48	2	Rosario	Sabana	2026-05-02 22:05:00	3	1500	t	f	ACTIVE	2026-05-03 03:04:14.417792	COMPLETED
43	7	Javeriana	Chapinero	2026-05-02 20:20:00	2	3000	t	f	ACTIVE	2026-05-03 01:18:27.464408	COMPLETED
50	2	Javeriana	Chia	2026-05-04 06:31:00	1	4500	t	f	ACTIVE	2026-05-04 11:16:26.515079	COMPLETED
45	2	Rosario	Suba	2026-05-02 21:12:00	4	4000	t	f	ACTIVE	2026-05-03 02:08:32.002905	COMPLETED
46	2	Nacional	Chapinero	2026-05-02 21:25:00	4	5500	t	f	ACTIVE	2026-05-03 02:16:21.23073	COMPLETED
49	3	Javeriana	Cartagena	2026-05-02 22:14:00	2	20000	f	f	ACTIVE	2026-05-03 03:10:59.183841	COMPLETED
36	2	Javeriana	Chapinero	2026-05-03 10:51:00	0	4000	t	f	ACTIVE	2026-05-02 22:51:41.246923	COMPLETED
47	3	Javeriana	Andino	2026-05-03 08:30:00	2	20000	t	f	ACTIVE	2026-05-03 03:01:16.711262	COMPLETED
53	11	Javeriana	Nacional	2026-05-04 11:55:00	3	3000	t	f	ACTIVE	2026-05-04 16:44:07.501973	COMPLETED
52	2	Central	LALALALA	2026-05-04 07:04:00	2	20000	f	f	ACTIVE	2026-05-04 11:59:07.321216	COMPLETED
54	2	Javeriana	Chapinero	2026-05-05 11:55:00	2	4000	t	f	ACTIVE	2026-05-04 22:50:10.770179	ACTIVE
55	2	Javeriana	Chapinero	2026-05-04 19:13:00	3	2500	t	f	ACTIVE	2026-05-04 23:10:26.5607	COMPLETED
57	2	Javeriana	Chapinero	2026-05-04 19:00:00	3	4500	t	f	ACTIVE	2026-05-04 23:46:21.522883	COMPLETED
58	9	Javeriana	Chapinero	2026-05-04 19:15:00	3	3000	t	f	ACTIVE	2026-05-05 00:12:40.191968	COMPLETED
59	2	Javeriana	Chapinero	2026-05-05 03:10:00	3	4500	t	f	ACTIVE	2026-05-05 08:04:52.603319	COMPLETED
56	2	Javeriana	Chapinero	2026-05-05 18:45:00	2	2500	t	f	ACTIVE	2026-05-04 23:43:08.35311	COMPLETED
60	2	Javeriana	Chapinero	2026-05-05 03:27:00	3	3500	t	f	ACTIVE	2026-05-05 08:22:23.963177	COMPLETED
61	2	Javeriana	Chapinero	2026-05-07 14:18:00	2	4500	t	f	ACTIVE	2026-05-06 19:12:50.864161	ACTIVE
62	2	Javeriana	Chapinero	2026-05-07 14:33:00	2	2000	t	f	ACTIVE	2026-05-06 19:28:01.972536	ACTIVE
63	2	Javeriana	Chia	2026-05-07 14:56:00	3	3000	t	f	ACTIVE	2026-05-06 19:52:06.203853	ACTIVE
64	2	Central	Suba	2026-05-07 14:00:00	3	4500	f	f	ACTIVE	2026-05-06 19:56:07.122092	ACTIVE
65	2	Nacional	Chapinero	2026-05-07 14:05:00	3	7000	f	f	ACTIVE	2026-05-06 19:59:27.029253	ACTIVE
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: uniride_user
--

COPY public.users (id, full_name, email, password_hash, phone, rol, vehicle_plate, vehicle_color, rating, total_ratings, created_at) FROM stdin;
1	Usuario Prueba	usuarioprueba@javeriana.edu.co	$2a$10$UFJ/x.5I20eQLNU.Npki5e6h4i257JHIUvdZ9.EkcllZhULyMBUdG	3112223344	PASAJERO	\N	\N	5	0	2026-04-30 03:29:56.880038
3	Laura Valentina Lis	lisul@javeriana.edu.co	$2a$10$4xT/3RHmXsrk5qEEBLWDleQ2nGxIVDEuatitb/UhrTzlWDfntwBk6	3173109413	CONDUCTOR	BCE543	Negro	5	0	2026-04-30 03:41:34.40363
4	Perez	perez@javeriana.edu.co	$2a$10$CivmdoD/oSy/8JRjDNYlVO6lSwno2I50ZWoTtkijhupsHgUuYJnRW	3118302216	AMBOS	BJJ979	Verde	5	0	2026-04-30 02:01:40.950291
5	maria	maria@javeriana.edu.co	$2a$10$sa2zjAwBNcP8tk3A4CAhYOS1vE7nWZ5lJO8O7ZwbjrR9LLsUHLfLC	3118322216	PASAJERO	\N	\N	5	0	2026-04-30 02:49:50.239098
6	lina	lina@javeriana.edu.co	$2a$10$i9qZCVmIc0I4J7/wGf2Wzu5qEiqiYWMaWW53aq.XDAYN/X5E11x4C	3118332216	CONDUCTOR	BDD345	Amarrillo	5	0	2026-04-30 03:05:46.328787
8	Claudia Enciso	claudia@javeriana.edu.co	$2a$10$3b8aAbzemQN1pqcwRCOblezIzDPw9jQEMUuFkKmBdhD6EYo7XbNxq	3205202632	PASAJERO	\N	\N	5	0	2026-05-01 18:43:40.244781
9	esteban	esteban@javeriana.edu.co	$2a$10$hdLxp1/8OolApDdhmcW0KuwG3WuQPSDZ23FiQZ1k4mN/CxavQHKZC	3101130221	AMBOS	LLL111	Negro	5	0	2026-05-02 23:27:22.374928
10	juan	juan@javeriana.edu.co	$2a$10$Hmc4O1ILb18uwBpGLCNdKebgOlWICgl8/DazRaKUdwAG.6U25k616	3101130223	PASAJERO	\N	\N	5	0	2026-05-02 23:30:55.691947
7	luisa fernanda	luisa@javeriana.edu.co	$2a$10$oB4Gv7bgUhm.dEBHYd/Hqutz6a1Qc00TNAhjmSMQNIR7IZWmdwl0e	3128302216	CONDUCTOR	JLK234	Blanco	4.5	0	2026-04-30 10:49:59.433207
11	Sofia	sofia@javeriana.edu.co	$2a$10$VLNp1LQwe1ejBNCIQRbRM.Mv87ph7reLbvcmpKQEEgBWuoRVL1b16	3173119413	CONDUCTOR	BBB123	Rojo	5	0	2026-05-04 16:23:48.68383
2	Christian Becerra Enciso	christianbecerra@javeriana.edu.co	$2a$10$Yqzg124XBxKLpUe0bHHoZuWdxNVeDp1XBFl2AGp.iKh7DuiIJXhzm	3166651870	AMBOS	ABC134	Gris	3.111111111111111	0	2026-04-30 03:36:23.02611
\.


--
-- Name: bookings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: uniride_user
--

SELECT pg_catalog.setval('public.bookings_id_seq', 51, true);


--
-- Name: reviews_id_seq; Type: SEQUENCE SET; Schema: public; Owner: uniride_user
--

SELECT pg_catalog.setval('public.reviews_id_seq', 20, true);


--
-- Name: trips_id_seq; Type: SEQUENCE SET; Schema: public; Owner: uniride_user
--

SELECT pg_catalog.setval('public.trips_id_seq', 65, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: uniride_user
--

SELECT pg_catalog.setval('public.users_id_seq', 11, true);


--
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);


--
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- Name: trips trips_pkey; Type: CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.trips
    ADD CONSTRAINT trips_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_key; Type: CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_key UNIQUE (phone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: bookings bookings_passenger_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_passenger_id_fkey FOREIGN KEY (passenger_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: bookings bookings_trip_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_trip_id_fkey FOREIGN KEY (trip_id) REFERENCES public.trips(id) ON DELETE CASCADE;


--
-- Name: reviews reviews_reviewed_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_reviewed_id_fkey FOREIGN KEY (reviewed_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: reviews reviews_reviewer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_reviewer_id_fkey FOREIGN KEY (reviewer_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: trips trips_driver_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: uniride_user
--

ALTER TABLE ONLY public.trips
    ADD CONSTRAINT trips_driver_id_fkey FOREIGN KEY (driver_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- PostgreSQL database dump complete
--

\unrestrict t6sQcXv0cB2vww0Vvip9xjZzT7wcW0shL1vCWtHjuW9aTJGYretUZxdCZIZFpvU

