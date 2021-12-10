FROM postgres

ARG POSTGRES_PASSWORD
ENV POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
# EXPOSE 5432

# COPY services.sql /docker-entrypoint-initdb.d/services.sql
# RUN psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c '\set AUTOCOMMIT on'
RUN psql -U postgres -c "select 'Hello' AS '';"