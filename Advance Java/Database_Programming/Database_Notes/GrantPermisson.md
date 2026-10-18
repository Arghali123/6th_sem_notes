## How can i provide persmission of curd operation to usernane "Daenish"?
To grant full **CRUD** (Create, Read, Update, Delete) permissions to the user `"Daenish"`, you need to grant privileges for table operations (`INSERT`, `SELECT`, `UPDATE`, `DELETE`) as well as sequence permissions (so auto-incrementing ID columns work properly).

Run the following SQL commands in **pgAdmin**, **psql**, or your database management tool connected to the `studentdb` database:

---

### Step 1: Grant CRUD Permissions on All Tables

```sql
-- Grant CRUD permissions on all existing tables in the public schema
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO "Daenish";

-- Grant sequence permissions so auto-incrementing IDs (SERIAL / BIGSERIAL) work
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO "Daenish";

```

> **Note:** Remember to keep `"Daenish"` in double quotes so PostgreSQL preserves the exact uppercase letter **D**.

---

### Step 2: Ensure Future Tables Automatically Inherit Permissions (Recommended)

By default, permissions only apply to existing tables. To ensure `"Daenish"` automatically gets CRUD permissions on any new tables or sequences created in the future, run:

```sql
-- Grant default CRUD privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public 
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "Daenish";

-- Grant default privileges for future sequences
ALTER DEFAULT PRIVILEGES IN SCHEMA public 
GRANT USAGE, SELECT ON SEQUENCES TO "Daenish";

```

---

### Alternative: Make "Daenish" the Table Owner

If `"Daenish"` is the main developer account for this project, you can also make `"Daenish"` the owner of the `students` table, which grants full control over it:

```sql
ALTER TABLE students OWNER TO "Daenish";

```

After running these statements, your Java application will be able to perform `SELECT`, `INSERT`, `UPDATE`, and `DELETE` queries without any permission issues!