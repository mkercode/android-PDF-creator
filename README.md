# android-PDF-creator
An app that creates a PDF file from user input and saves it to the external storage. Worked with API30/Android 11.

String input is collected from an EditText with an onClick event, and written to a PDF file on the devices storage. 
Filenames include the current time and date in a yyyy:MM:dd:HH:mm:ss format to avoid overwritting files. 
A toast message is also displayed to tell the user if the operation was succesful or not. 
As this example implements scoped storage, if the app is uninstalled the files will be deleted.

Depending on your device, you may find the files in:
sdcard>Android>data>**app package name**>Files>PDF_files, 
storage>self>primary>Android>data>**app package name**>Files>PDF_files, OR
storage>emulated>Android>data>**app package name**>Files>PDF_files 

01/27/2021 Changes:
-File viewer functionality. You can view the names of the files you created in a seperate activity
-Added ability to delete files in the file viewer from the phones storage

02/02/2021 Changes:
- Added PDF viewer from barteksc, the application can view PDF files created in a seperate activity
- Visual changes

02/05/2021 Changes:
-Added editor functionality (thanks to the PDF text parses by iText)
-Added toolbars
-Visual changes and cute icons

Future changes will involve:
-Fragments instead of seperate activities
-Modern functionality changes such as view binding and naviagational component
