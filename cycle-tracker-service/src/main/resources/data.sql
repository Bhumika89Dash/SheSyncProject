-- ============================
-- CYCLE TRACKER : cycle_log
-- ============================

CREATE TABLE IF NOT EXISTS cycle_log (
                                         id UUID PRIMARY KEY,
                                         patient_id UUID NOT NULL,
                                         start_date DATE NOT NULL,
                                         end_date DATE,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Index for fast per-patient queries
CREATE INDEX IF NOT EXISTS idx_cycle_patient_date
    ON cycle_log (patient_id, start_date DESC);
-- ===============================
-- RIYA (Regular 28–29 day cycle)
-- ===============================

INSERT INTO cycle_log (id, patient_id, start_date, end_date, created_at)
VALUES
    ('a1a11111-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     '11111111-1111-1111-1111-111111111111',
     '2025-10-01', '2025-10-05', NOW() - INTERVAL '90 days'),

    ('a1a22222-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     '11111111-1111-1111-1111-111111111111',
     '2025-10-29', '2025-11-02', NOW() - INTERVAL '62 days'),

    ('a1a33333-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     '11111111-1111-1111-1111-111111111111',
     '2025-11-26', '2025-11-30', NOW() - INTERVAL '34 days'),

    ('a1a44444-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     '11111111-1111-1111-1111-111111111111',
     '2025-12-24', '2025-12-28', NOW() - INTERVAL '6 days');



-- ===============================
-- ANANYA (Irregular cycle)
-- ===============================

INSERT INTO cycle_log (id, patient_id, start_date, end_date, created_at)
VALUES
    ('b2b11111-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
     '22222222-2222-2222-2222-222222222222',
     '2025-09-15', '2025-09-20', NOW() - INTERVAL '105 days'),

    ('b2b22222-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
     '22222222-2222-2222-2222-222222222222',
     '2025-10-20', '2025-10-25', NOW() - INTERVAL '70 days'),

    ('b2b33333-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
     '22222222-2222-2222-2222-222222222222',
     '2025-12-01', '2025-12-06', NOW() - INTERVAL '28 days');



-- ===============================
-- KRITI (New user – only 1 cycle)
-- ===============================

INSERT INTO cycle_log (id, patient_id, start_date, end_date, created_at)
VALUES
    ('c3c11111-cccc-cccc-cccc-cccccccccccc',
     '33333333-3333-3333-3333-333333333333',
     '2025-12-20', '2025-12-24', NOW() - INTERVAL '10 days');



-- =====================================================
-- DROP TABLES (for clean re-runs in dev only)
-- =====================================================
DROP TABLE IF EXISTS symptom_log;
DROP TABLE IF EXISTS health_profile;

-- =====================================================
-- TABLE 1: health_profile
-- One row per user (baseline health info)
-- =====================================================
CREATE TABLE health_profile (
                                user_id UUID PRIMARY KEY,

                                known_pcos BOOLEAN,
                                thyroid BOOLEAN,
                                diabetes BOOLEAN,

                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLE 2: symptom_log
-- Multiple rows per user (daily / periodic logs)
-- =====================================================
CREATE TABLE symptom_log (

                             user_id UUID PRIMARY KEY,

                             log_date DATE NOT NULL,

                             mood VARCHAR(50),
                             pain_level INT CHECK (pain_level BETWEEN 0 AND 10),
                             fatigue BOOLEAN,
                             bloating BOOLEAN,
                             cramps BOOLEAN,
                             stress_level INT CHECK (stress_level BETWEEN 0 AND 10),

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- SAMPLE USERS (UUIDs come from Patient/Auth Service)
-- =====================================================
-- User A: Regular cycles
-- User B: Possible hormonal issues
-- User C: New user (minimal data)

-- =====================================================
-- INSERT INTO health_profile
-- =====================================================
INSERT INTO health_profile (user_id, known_pcos, thyroid, diabetes)
VALUES
-- Existing user
('11111111-1111-1111-1111-111111111111', false, false, false),

-- Existing user with PCOS
('22222222-2222-2222-2222-222222222222', true, false, false),

-- Newly registered user
('33333333-3333-3333-3333-333333333333', null, null, null);

-- =====================================================
-- INSERT INTO symptom_log
-- =====================================================
INSERT INTO symptom_log (
    user_id,
    log_date,
    mood,
    pain_level,
    fatigue,
    bloating,
    cramps,
    stress_level
)
VALUES
-- ===== User A logs =====
(

    '11111111-1111-1111-1111-111111111111',
    CURRENT_DATE - INTERVAL '5 days',
    'HAPPY',
    2,
    false,
    false,
    false,
    3
),
(

    '11111111-1111-1111-1111-111111111111',
    CURRENT_DATE - INTERVAL '2 days',
    'CALM',
    1,
    false,
    false,
    false,
    2
),

-- ===== User B logs (more symptoms) =====
(

    '22222222-2222-2222-2222-222222222222',
    CURRENT_DATE - INTERVAL '7 days',
    'IRRITABLE',
    7,
    true,
    true,
    true,
    8
),
(

    '22222222-2222-2222-2222-222222222222',
    CURRENT_DATE - INTERVAL '3 days',
    'LOW',
    6,
    true,
    true,
    false,
    7
),

-- ===== User C (new user – single log) =====
(

    '33333333-3333-3333-3333-333333333333',
    CURRENT_DATE - INTERVAL '1 day',
    'NEUTRAL',
    3,
    false,
    false,
    false,
    4
);

-- =====================================================
-- END OF FILE
-- =====================================================
