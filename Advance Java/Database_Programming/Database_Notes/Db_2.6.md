# Topic 6 — Scrollable and Updatable ResultSets; RowSets in Java Database Programming.

# 1. Learning objectives

By the end of this lesson, you should understand:

- Ordinary versus scrollable `ResultSet`.
- How to navigate rows using `first()`, `last()`, `previous()`, and `absolute()`.
- How to update database records through an updatable `ResultSet`.
- What `RowSet` is and how it differs from `ResultSet`.
- How to write and run complete Java programs using PostgreSQL.

## 2. Understanding the ordinary `ResultSet`

When you execute a `SELECT` query, JDBC typically returns a `ResultSet` that behaves like a cursor over the query results.

Imagine your `students` table contains:

| ID | Name  | Marks |
| -- | ----- | ----- |
| 1  | Aarav | 85    |
| 2  | Sita  | 92    |
| 3  | Ram   | 76    |
| 4  | Mina  | 88    |

A typical forward-only `ResultSet` starts before the first row. Calling `next()` moves the cursor forward one row at a time.

Visualize the cursor

Before firstnext()

1

Aarav

Marks: 85

2

Sita

Marks: 92

3

Ram

Marks: 76

4

Mina

Marks: 88

The cursor is before the first row.

The limitation is that a forward-only result set normally cannot move backward to a row already visited. Scrollable result sets provide additional navigation methods.

## 3. Scrollable `ResultSet`

A scrollable `ResultSet` allows you to move the cursor forward and backward, or jump to a particular row.

The official Java API defines two common scrollable types: `TYPE_SCROLL_INSENSITIVE` and `TYPE_SCROLL_SENSITIVE`.



### A. ResultSet types

| Type                      | Meaning                                                                                                 |
| ------------------------- | ------------------------------------------------------------------------------------------------------- |
| `TYPE_FORWARD_ONLY`       | Moves forward only.                                                                                     |
| `TYPE_SCROLL_INSENSITIVE` | Can scroll both ways; generally doesn't reflect changes made by others after the result set is created. |
| `TYPE_SCROLL_SENSITIVE`   | Can scroll both ways and may reflect changes made by others, depending on driver support.               |

### B. Important cursor methods

| Method          | Purpose                                                 |
| --------------- | ------------------------------------------------------- |
| `next()`        | Move to the next row                                    |
| `previous()`    | Move to the previous row                                |
| `first()`       | Move to the first row                                   |
| `last()`        | Move to the last row                                    |
| `absolute(3)`   | Move to row 3                                           |
| `absolute(-1)`  | Move to the last row                                    |
| `relative(1)`   | Move one row forward from the current position          |
| `beforeFirst()` | Move before the first row                               |
| `afterLast()`   | Move after the last row                                 |
| `getRow()`      | Get the current row number; returns `0` if not on a row |

### C. Complete practical: navigate student records

Create `ScrollableStudents.java`.

```

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScrollableStudents {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (username == null || password == null) {
            System.out.println("Set DB_USERNAME and DB_PASSWORD first.");
            return;
        }

        String sql = "SELECT id, name, marks FROM students ORDER BY id";

        try (Connection con =
                     DriverManager.getConnection(url, username, password);
             Statement st = con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = st.executeQuery(sql)) {

            // Move to the first row
            if (rs.first()) {
                System.out.println("First student: "
                        + rs.getString("name"));
            }

            // Move to the last row
            if (rs.last()) {
                System.out.println("Last student: "
                        + rs.getString("name"));
            }

            // Move directly to row number 2
            if (rs.absolute(2)) {
                System.out.println("Student at row 2: "
                        + rs.getString("name"));
            }

            // Move backward one row
            if (rs.previous()) {
                System.out.println("Previous student: "
                        + rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

```

What is important here?

```
con.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_READ_ONLY
);
```

- `TYPE_SCROLL_INSENSITIVE` enables scrolling.
- `CONCUR_READ_ONLY` means you can read rows but cannot update them through this `ResultSet`.

We will change the second option in the next section to enable updates.

## 4. Updatable `ResultSet`

An updatable `ResultSet` allows you to modify a row through JDBC result-set methods rather than writing a separate `UPDATE` statement.

To request one, use:

```
Statement st = con.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,
    ResultSet.CONCUR_UPDATABLE
);
```

The important constant is `CONCUR_UPDATABLE`. It requests an updatable result set; the database and JDBC driver must support the operation.


### How updating works

1\. Retrieve the row

Use `rs.next()` or another cursor method.

2\. Set the new value

Use `rs.updateDouble()` or `rs.updateString()`.

3\. Apply the update

Call `rs.updateRow()` to write the changes to the database.

### Complete practical: update a student's marks

This example changes Sita's marks to `95`. It modifies your real database, so run it only if you intend to make that change.

```

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateStudentResultSet {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (username == null || password == null) {
            System.out.println("Set DB_USERNAME and DB_PASSWORD first.");
            return;
        }

        String sql = "SELECT id, name, marks FROM students ORDER BY id";

        try (Connection con =
                     DriverManager.getConnection(url, username, password);
             Statement st = con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = st.executeQuery(sql)) {

            boolean found = false;

            while (rs.next()) {
                if ("Sita".equals(rs.getString("name"))) {

                    rs.updateDouble("marks", 95.0);
                    rs.updateRow();

                    System.out.println(
                            "Sita's marks updated to 95.");
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Student Sita was not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

```

### Understand the three key methods

```
rs.updateDouble("marks", 95.0);
```

Specifies the new value for the `marks` column in the current row.

```
rs.updateRow();
```

Writes the pending column updates to the database.

```
rs.getDouble("marks");
```

Reads the marks from the current row.

Remember: calling `updateDouble()` alone does not apply the change to the database. Call `updateRow()` after setting the new values.

Updatable result sets also have limitations: some query shapes, joins, or driver/database combinations may not support updates. For PostgreSQL, a simple query selecting columns from one table is a good starting point.

## 5. What is a `RowSet`?

A `RowSet` is an interface that extends `ResultSet` and adds features such as JavaBeans-style properties and configuration. Different implementations offer different ways to manage database rows.


Think of the distinction this way:

- `ResultSet`: represents the rows returned by executing a JDBC statement.
- `RowSet`: adds a more configurable interface and has implementations that can remain connected to, or disconnect from, the database.

### Main types of `RowSet`

| Type             | Purpose                                                                                                                             |
| ---------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `JdbcRowSet`     | Connected row set; wraps result-set functionality and supports scrolling and updates.                                               |
| `CachedRowSet`   | Stores rows in memory and can operate while disconnected from the database. Changes can later be synchronized with the data source. |
| `WebRowSet`      | A `CachedRowSet` extension that can read and write row-set data as XML.                                                             |
| `FilteredRowSet` | Supports filtering rows using a predicate.                                                                                          |
| `JoinRowSet`     | Combines related rows from multiple row sets.                                                                                       |

These are standard JDBC row-set types described in the Java API documentation.


### A. `JdbcRowSet` — connected example

This example retrieves students using `JdbcRowSet`, then moves to the last row.

```

import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class JdbcRowSetExample {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (username == null || password == null) {
            System.out.println("Set DB_USERNAME and DB_PASSWORD first.");
            return;
        }

        try (JdbcRowSet rowSet =
                     RowSetProvider.newFactory().createJdbcRowSet()) {

            rowSet.setUrl(url);
            rowSet.setUsername(username);
            rowSet.setPassword(password);
            rowSet.setCommand(
                    "SELECT id, name, marks FROM students ORDER BY id");

            rowSet.execute();

            if (rowSet.last()) {
                System.out.println("Last student: "
                        + rowSet.getString("name"));
                System.out.println("Marks: "
                        + rowSet.getDouble("marks"));
            } else {
                System.out.println("No students found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

```

The call to `execute()` establishes the connection and runs the configured command. The `JdbcRowSet` then provides methods such as `last()` and `getString()`.



### B. `CachedRowSet` — why disconnected data matters

A `CachedRowSet` can hold a copy of the result data in memory. This means your application can navigate or work with those cached rows after the original database connection has been closed. If you edit the cached data, synchronizing it back to the database is a separate step.

This is useful when an application needs to work with data without holding a database connection open all the time. However, synchronization can fail if the underlying database records have changed or conflicts occur.

## 6. Compare the concepts

| Feature                   | Scrollable `ResultSet`         | Updatable `ResultSet`             | `JdbcRowSet`                   | `CachedRowSet`                |
| ------------------------- | ------------------------------ | --------------------------------- | ------------------------------ | ----------------------------- |
| Move backward             | Yes, if scrolling is supported | Yes, if configured as scrollable  | Yes                            | Yes                           |
| Modify rows               | Only if updatable              | Yes, if supported                 | Supports updates               | Supports cached updates       |
| Holds database connection | Yes, while connected           | Yes, while connected              | Yes                            | Can operate disconnected      |
| Main benefit              | Flexible navigation            | Modify records through the cursor | Configurable connected row set | Work with cached data offline |
