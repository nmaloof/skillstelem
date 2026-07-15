-- create table tool_calls (
--     id integer primary key autoincrement,
--     source text not null,
--     session_id text not null,
--     skill_name text not null,
--     called_at text not null default (strftime('%Y-%m-%dT%H:%M:%fZ', 'now'))
-- );
create table tool_calls (
    id bigserial primary key,
    source text not null,
    session_id text not null,
    skill_name text not null,
    called_at timestamptz not null default (now() at time zone 'UTC')
);