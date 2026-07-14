export PGDATA="$PWD/.pg"
export PGHOST="$PGDATA"

if [ ! -d "$PGDATA" ]; then
    initdb --no-locale --encoding=UTF8
    echo "unix_socket_directories = '$PGDATA'" >> "$PGDATA/postgresql.conf"
    echo "listen_addresses = ''" >> "$PGDATA/postgresql.conf"
fi

if ! pg_ctl status > /dev/null 2>&1; then
    pg_ctl start -l "$PGDATA/log"

    if ! psql -lqt | cut -d\| -f1 | grep -qw skills_telem; then
        createdb skills_telem
    fi
fi

trap "pg_ctl stop -m fast 2>/dev/null" EXIT