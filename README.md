# Byte-Cast-Productivity-App

To run:
  1. Navigate to DesktopAppApplication.java
  2. Right-Click and select "run DesktopAppAppl...main()"

If there is an error make sure your SDK and environment is set to Java 17:
  1. Open a new command prompt
  2. Type "java -version" and hit enter
  3. If it does not show Java 17 then google it and follow the instructions to install
  4. Once installed repeat step 2
  5. If it still does not show, go to file explorer and right-click "This PC" and select properties
  6. Then select "Advanced system settings"
  7. Click on Environment variables
  8. In "System Variables" click "New"
  9. Enter "JAVA_HOME" for Variable Name and "C:\Program Files\Java\jdk-17" for Variable Value
  10. Then click "Path" under System Variables and click "Edit"
  11. If there is no "%JAVA_HOME%\bin" click "New" and in the box enter "%JAVA_HOME%\bin"
  12. Move it to the top of the window by selected it and clicking "Move up" until it is the first item.
  13. Click "Ok" to save and apply all changes and close out of the windows.
  14. Open a new command prompt and type "java -version" and "javac -version"
  15. Make sure both show Java 17
