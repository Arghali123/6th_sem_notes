# Topic 8 — SQL Escapes

Unit 2 · Java Database Programming · Java + PostgreSQL

## 1. What is SQL escape syntax?

SQL escape syntax is a JDBC feature that lets you write certain database operations in a standard form. The JDBC driver processes the special syntax and translates it into SQL understood by the database. This can make some SQL features more portable across different database systems.



A JDBC escape typically uses curly braces and a keyword:

```
{keyword parameters}
```

For example, a JDBC date escape is:

```
{d '2026-10-15'}
```

The PostgreSQL JDBC driver can translate this into native PostgreSQL syntax equivalent to:

```
DATE '2026-10-15'
```

Why do we need it? Different database systems sometimes use different syntax for functions, date/time literals, and other operations. JDBC escapes provide a standard way to express supported features.

## 2. Important types of JDBC escapes

\| Escape | Purpose | Example | |---|---|---| | `{d ...}` | Date literal | `{d '2026-10-15'}` | | `{t ...}` | Time literal | `{t '10:30:00'}` | | `{ts ...}` | Timestamp literal | `{ts '2026-10-15 10:30:00'}` | | `{fn ...}` | Scalar function | `{fn UCASE(name)}` | | `{oj ...}` | Outer join syntax | `{oj A LEFT OUTER JOIN B ON A.id=B.id}` | | `{escape ...}` | Specify a `LIKE` escape character | `LIKE '|_%' {escape '|'}` | | `{call ...}` | Call a stored procedure/function through JDBC | `{call my_procedure(?)}` |

The syntax and actual support can depend on the JDBC driver and database. PostgreSQL's pgJDBC documentation describes these escapes and their translations.


## 3. Practical example — date and function escapes

Let's use two escape types in one simple Java program:

- `{d '2026-10-15'}` — a date literal.
- `{fn UCASE('hello')}` — a standard JDBC function escape for uppercase conversion.

Create `SqlEscapesDemo.java` in your VS Code project.

```

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlEscapesDemo {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (username == null || password == null) {
            System.out.println("Set DB_USERNAME and DB_PASSWORD first.");
            return;
        }

        String sql =
                "SELECT {d '2026-10-15'} AS sample_date, " +
                "{fn UCASE('hello')} AS upper_text";

        try (Connection con =
                     DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println(
                        "Date: " + rs.getDate("sample_date"));
                System.out.println(
                        "Uppercase: " + rs.getString("upper_text"));
            }

        } catch (SQLException e) {
            System.out.println("SQL operation failed.");
            e.printStackTrace();
        }
    }
}

```

Expected output:

```
Date: 2026-10-15
Uppercase: HELLO
```

The driver processes the escape syntax before PostgreSQL executes the query. The exact translation is driver-dependent, but pgJDBC supports these date and uppercase-function escapes.



### How does it work?

1\. Java SQL string

{d '2026-10-15'}

{fn UCASE('hello')}

2\. JDBC driver processes escapes

Translates supported escape syntax

3\. PostgreSQL executes the query

Returns a date and the text `HELLO`

## 4. Important distinction: SQL escapes are not SQL injection protection

Don't confuse these two concepts:

- JDBC SQL escapes: standardize certain SQL syntax, such as date literals and function calls.
- `PreparedStatement` parameters: safely pass data values using `?` placeholders.

For example, use `PreparedStatement` parameters for user-provided student names instead of concatenating them into SQL. SQL escapes do not replace parameterized queries.

You can also disable escape processing for a `Statement` using `setEscapeProcessing(false)`, but it is normally enabled by default.

