# Inventory Management JavaFX App

Java application for the workshop activity. It uses multiple JavaFX views, a JDBC database connection, and `.env` configuration for database credentials.

## Features

- Login view
- Signup view
- Inventory dashboard view
- Add, update, delete, and refresh inventory items
- SQLite database connection through JDBC
- Password hashing with BCrypt
- Database URL/user/password loaded from `.env`

## Setup

1. Copy `.env.example` to `.env`.
2. Keep the default SQLite config or update it for your own JDBC database.
3. Run the app:

```bash
mvn javafx:run
```

You can also open the folder in IntelliJ IDEA, NetBeans, or VS Code with the Java extensions, then run the Maven goal `javafx:run`.

## Database

The app automatically creates these tables when it starts:

- `users`
- `items`

For SQLite, the local `inventory.db` file is generated automatically and ignored by Git.
