export PGDATA="$PWD/.pg"
export PGHOST="$PGDATA"
export PGPORT=5432

if [ ! -d "$PGDATA" ]; then
    initdb --no-locale --encoding=UTF8
    {
        echo "unix_socket_directories = '$PGDATA'"
        echo "listen_addresses = 'localhost'" 
    } >> "$PGDATA/postgresql.conf"    
fi

if ! pg_ctl status > /dev/null 2>&1; then
    pg_ctl start -l "$PGDATA/log"

    if ! psql -lqt | cut -d\| -f1 | grep -qw skills_telem; then
        createdb skills_telem

        psql -d skills_telem -c "CREATE USER appuser WITH PASSWORD 'apppassword';"
        psql -d skills_telem -c "GRANT ALL PRIVILEGES ON DATABASE skills_telem to appuser;"
        psql -d skills_telem -c "GRANT ALL ON SCHEMA public to appuser;"
        psql -d skills_telem -c "ALTER DEFAULT PRIVILEGES GRANT ALL ON TABLES to appuser;"
        psql -d skills_telem -c "ALTER DEFAULT PRIVILEGES GRANT ALL ON SEQUENCES to appuser;"
    fi
fi

trap "pg_ctl stop -m fast 2>/dev/null" EXIT