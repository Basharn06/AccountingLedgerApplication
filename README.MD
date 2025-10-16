# 🥯 Bagel Café Accounting Ledger Application

## 📘 About the Project
The **Bagel Café Accounting Ledger Application** is a Java console-based program that helps small café owners manage and track their **daily income and expenses**.  

This application allows users to:
- Record **customer sales (deposits)**
- Record **wholesale supply purchases (payments)**
- View **ledger entries** in multiple ways
- Generate **financial reports** by date or vendor
- Automatically save all data to a CSV file  

The program stores all financial data in a file named **`transactions.csv`**, allowing users to reopen the program without losing past records.

---

## 🧾 Features
- 💰 **Add Deposits:** Record customer payments and daily café sales  
- 💳 **Add Payments:** Track wholesale and supply orders  
- 📒 **Ledger Menu:** View all transactions or filter by deposits and payments  
- 📊 **Reports Menu:** Generate business reports by:
  - Month To Date  
  - Previous Month  
  - Year To Date  
  - Previous Year  
  - Search by Vendor  
- 💾 **Auto Save System:** All data is automatically written to `transactions.csv`

---

## 🖥️ Application Screens

### 🏠 Home Menu
```
Welcome To The Accounting Ledger:
Please select one of the options below 

D) Add Deposit 
P) Make Payment (Debit)
L) Ledger - display the ledger screen
X) Exit - exit the application
```

### 📒 Ledger Menu
```
Ledger Menu:
A) All - Display all entries
D) Deposits - Display only deposits
P) Payments - Display only payments
R) Reports - View reports and searches
H) Home - Go back to the home page
```

### 📊 Reports Menu
```
Reports Menu:
1) Month To Date
2) Previous Month
3) Year To Date
4) Previous Year
5) Search by Vendor
0) Back - Return to Ledger Page
```

---

## 📂 Files in This Project
| File | Description |
|------|--------------|
| `accountingLedgerApplication.java` | Main program that handles user input, menus, and data management |
| `data.java` | Class that defines each transaction with fields like date, time, vendor, description, and amount |
| `transactions.csv` | File where all café deposits and payments are saved |

---

## 🧱 Technologies Used
- **Language:** Java  
- **Libraries:** `java.util`, `java.io`, `java.time`, `java.math`  
- **Storage Format:** CSV File  
- **IDE:** IntelliJ IDEA  

---

## ⚙️ How to Run the Program

### Step 1: Open IntelliJ IDEA  
Create a new **Java Project** called `BagelCafeLedger`.

### Step 2: Add Files  
Inside the `src/com/pluralsight/` folder, add these two files:
- `accountingLedgerApplication.java`
- `data.java`

### Step 3: Add the CSV File  
Create a new file named **`transactions.csv`** in your main project folder.  
You can start it empty or add your own café transactions later.

### Step 4: Run the Program  
Click **Run ▶️** in IntelliJ.  
The home menu will appear in your console.

### Step 5: Use the Menu  
- Press **D** to add deposits (daily sales)  
- Press **P** to add payments (supply purchases)  
- Press **L** to open your ledger and reports  


 
