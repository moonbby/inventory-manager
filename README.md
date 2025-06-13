# Inventory Management System

A Java Swing-based desktop application for managing product inventory with an embedded Apache Derby database and a clean GUI.

---

## Features

- Add, remove, restock, and purchase products
- Summary report, most expensive product, and low-stock export
- One-click inventory backup
- View complete log history of actions
- Developer tools to reset product, log, and backup tables
- Input validation with friendly error messages
- Consistent GUI theming via a custom `ThemeManager`
- Built with full OOP principles (abstraction, inheritance, encapsulation, polymorphism)
- Includes unit tests and Git version history

---

## Technologies

- Java 21  
- Swing (GUI)  
- Apache Derby Embedded DB  
- JUnit 4  
- Git

---

## How to Run

1. Open the project in NetBeans or IntelliJ  
2. Ensure the `/database/` folder is in the project root  
3. Run `InventorySystem.java` (in `gui` package)  
4. **No login required** — app launches directly

---

## Database Details

- **DB URL**: `jdbc:derby://localhost:1527/inventoryDB`  

> Note: Derby DB does not enforce authentication; any credentials will work.

---

## Testing

- Includes 5 unit tests covering:
  - `InventoryManager`
  - `ReportManager`
  - `BackupManager`
  - `LogManager`
  - `ProductDAO`
- Tests auto-reset DB with seeded values via `InventoryTableSeeder`
