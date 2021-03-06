/* $PostgreSQL: pgsql/contrib/pgcrypto/pgcrypto.sql.in,v 1.15 2007/11/13 04:24:28 momjian Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

CREATE OR REPLACE FUNCTION digest(text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_digest'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION digest(bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_digest'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION hmac(text, text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_hmac'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION hmac(bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_hmac'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION crypt(text, text)
RETURNS text
AS '$libdir/pgcrypto', 'pg_crypt'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gen_salt(text)
RETURNS text
AS '$libdir/pgcrypto', 'pg_gen_salt'
LANGUAGE C VOLATILE STRICT;

CREATE OR REPLACE FUNCTION gen_salt(text, int4)
RETURNS text
AS '$libdir/pgcrypto', 'pg_gen_salt_rounds'
LANGUAGE C VOLATILE STRICT;

CREATE OR REPLACE FUNCTION encrypt(bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_encrypt'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION decrypt(bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_decrypt'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION encrypt_iv(bytea, bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_encrypt_iv'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION decrypt_iv(bytea, bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_decrypt_iv'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gen_random_bytes(int4)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_random_bytes'
LANGUAGE 'C' VOLATILE STRICT;

--
-- pgp_sym_encrypt(data, key)
--
CREATE OR REPLACE FUNCTION pgp_sym_encrypt(text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_sym_encrypt_text'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pgp_sym_encrypt_bytea(bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_sym_encrypt_bytea'
LANGUAGE C STRICT;

--
-- pgp_sym_encrypt(data, key, args)
--
CREATE OR REPLACE FUNCTION pgp_sym_encrypt(text, text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_sym_encrypt_text'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pgp_sym_encrypt_bytea(bytea, text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_sym_encrypt_bytea'
LANGUAGE C STRICT;

--
-- pgp_sym_decrypt(data, key)
--
CREATE OR REPLACE FUNCTION pgp_sym_decrypt(bytea, text)
RETURNS text
AS '$libdir/pgcrypto', 'pgp_sym_decrypt_text'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION pgp_sym_decrypt_bytea(bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_sym_decrypt_bytea'
LANGUAGE C IMMUTABLE STRICT;

--
-- pgp_sym_decrypt(data, key, args)
--
CREATE OR REPLACE FUNCTION pgp_sym_decrypt(bytea, text, text)
RETURNS text
AS '$libdir/pgcrypto', 'pgp_sym_decrypt_text'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION pgp_sym_decrypt_bytea(bytea, text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_sym_decrypt_bytea'
LANGUAGE C IMMUTABLE STRICT;

--
-- pgp_pub_encrypt(data, key)
--
CREATE OR REPLACE FUNCTION pgp_pub_encrypt(text, bytea)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_encrypt_text'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pgp_pub_encrypt_bytea(bytea, bytea)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_encrypt_bytea'
LANGUAGE C STRICT;

--
-- pgp_pub_encrypt(data, key, args)
--
CREATE OR REPLACE FUNCTION pgp_pub_encrypt(text, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_encrypt_text'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pgp_pub_encrypt_bytea(bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_encrypt_bytea'
LANGUAGE C STRICT;

--
-- pgp_pub_decrypt(data, key)
--
CREATE OR REPLACE FUNCTION pgp_pub_decrypt(bytea, bytea)
RETURNS text
AS '$libdir/pgcrypto', 'pgp_pub_decrypt_text'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION pgp_pub_decrypt_bytea(bytea, bytea)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_decrypt_bytea'
LANGUAGE C IMMUTABLE STRICT;

--
-- pgp_pub_decrypt(data, key, psw)
--
CREATE OR REPLACE FUNCTION pgp_pub_decrypt(bytea, bytea, text)
RETURNS text
AS '$libdir/pgcrypto', 'pgp_pub_decrypt_text'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION pgp_pub_decrypt_bytea(bytea, bytea, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_decrypt_bytea'
LANGUAGE C IMMUTABLE STRICT;

--
-- pgp_pub_decrypt(data, key, psw, arg)
--
CREATE OR REPLACE FUNCTION pgp_pub_decrypt(bytea, bytea, text, text)
RETURNS text
AS '$libdir/pgcrypto', 'pgp_pub_decrypt_text'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION pgp_pub_decrypt_bytea(bytea, bytea, text, text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pgp_pub_decrypt_bytea'
LANGUAGE C IMMUTABLE STRICT;

--
-- PGP key ID
--
CREATE OR REPLACE FUNCTION pgp_key_id(bytea)
RETURNS text
AS '$libdir/pgcrypto', 'pgp_key_id_w'
LANGUAGE C IMMUTABLE STRICT;

--
-- pgp armor
--
CREATE OR REPLACE FUNCTION armor(bytea)
RETURNS text
AS '$libdir/pgcrypto', 'pg_armor'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION dearmor(text)
RETURNS bytea
AS '$libdir/pgcrypto', 'pg_dearmor'
LANGUAGE C IMMUTABLE STRICT;

