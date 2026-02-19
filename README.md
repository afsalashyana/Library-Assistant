# Material Design Library Management Software using JavaFX
This is a library management software developed using JavaFX programming language. The entire development video with explanation of each and every part (in realtime) is available in my YouTube Channel [Genuine Coder Youtube Channel](https://www.youtube.com/playlist?list=PLhs1urmduZ29jTcE1ca8Z6bZNvH_39ayL).
  
### New Features (v1.1.0)
* **Data Export**: Export Book and Member lists to CSV format for easy reporting.
* **Email Validation**: Enhanced validation for member email addresses during registration.
* **Release Automation**: One-click packaging using `package_app.ps1`.

### Previous Features
* One-click database export
  <p align="center">
   <img src=https://i.imgur.com/ufZOqkn.png>
  </p>
* Notify overdue via email.
  <p align="center">
   <img src=https://i.imgur.com/7UutZZQ.png>
  </p>
* Rich text email notification content.
   <p align="center">
    <img src=https://i.imgur.com/jj9Lk5G.png>
   </p>
* AES Encryption for Mail Server Configuration - AES/CBC/PKCS5Padding   
   <p align="center">
    <img src=https://i.imgur.com/WrWZqLr.png>
   </p>

### Getting Started

#### Prerequisites
* Java 8 (JDK 1.8)
* Maven (optional, for dependency management if not using included libs)

#### Running the Application
To run the application locally:
```powershell
.\run_app.ps1
```

#### Building from Source
To create a release package (ZIP with JAR and dependencies):
```powershell
.\package_app.ps1
```
The output will be available in the `dist` folder.

### Default Login Credentials
| Username  | Password |
| ------------- | ------------- |
| admin  | admin  |

### Libraries Used
  * [JFoenix](https://github.com/jfoenixadmin/JFoenix) - JavaFX Material Design Library
  * [Apache Derby](https://db.apache.org/derby/) - Standalone Relational database
  * [Apache Commons](https://commons.apache.org/) - For creating SHA hash and CSV Export
  * [GSon](https://github.com/google/gson) - JSON Library. Used for storing configuration
  * [FontawesomeFX](https://bitbucket.org/Jerady/fontawesomefx) - Icon library
  * [Apache PDFBox](https://pdfbox.apache.org/) - PDF Export
  * [JavaMail API](http://www.oracle.com/technetwork/java/javamail/index.html) - Email Notification

### Screenshots
<p align="center">
  <img src=https://i.imgur.com/txmOeXS.png>
  <img src=https://i.imgur.com/Ezj7Bdh.png>
  <img src=https://i.imgur.com/YyK54nF.png>
  <img src=https://i.imgur.com/0wCfUjQ.png>
  <img src=https://i.imgur.com/E4OhaWl.png>
  <img src=https://i.imgur.com/3WMG9Bm.png>
  <img src=https://i.imgur.com/3316yUv.png>
  <img src=https://i.imgur.com/y7jrx93.png>
  <img src=https://i.imgur.com/O0LXqoK.png>
</p>
