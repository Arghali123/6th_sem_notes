Good news! Your pgvector compilation appears to have succeeded. The problem is now with **Windows file permissions**, not with pgvector itself.

Look at this line:

```
copy vector.dll "C:\Program Files\PostgreSQL\18\lib"
Access is denied.
```

Windows is preventing the installer from copying `vector.dll` into PostgreSQL's protected installation directory.

Let's fix it.

## **Step 1 — Close your current command prompt**

Close the current `x64 Native Tools Command Prompt`.

## **Step 2 — Reopen it as Administrator**

1. Open Windows Start.
2. Search for **x64 Native Tools Command Prompt for VS 2022**.
3. Right-click it.
4. Select **Run as administrator**.
5. Accept the Windows permission prompt.



Important

The command prompt title should indicate that it is running as Administrator.


## **Step 3 — Navigate back to pgvector**

Execute:

```
cd %TEMP%\pgvector
```

Set your PostgreSQL installation path:

```
set "PGROOT=C:\Program Files\PostgreSQL\18"
```

## **Step 4 — Run the installation again**

Execute:

```
nmake /F Makefile.win install
```

This time, Windows should allow the installer to copy the files into PostgreSQL's directories.

## **Step 5 — Verify in pgAdmin**

Open pgAdmin and execute:

```
CREATE EXTENSION IF NOT EXISTS vector;
```

Then:

```
SELECT extname, extversion
FROM pg_extension
WHERE extname = 'vector';
```

You should see the `vector` extension installed.

**Your next task:** Try Steps 1–4 and share the output of `nmake /F Makefile.win install`.

Don't reinstall PostgreSQL or rebuild pgvector yet. Let's first solve this permissions issue! 😊