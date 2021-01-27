# android-PDF-creator
An app that creates a PDF file from user input and saves it to the external storage. Worked with API30/Android 11.

String input is collected from an EditText with an onClick event, and written to a PDF file on the devices storage. 
Filenames include the current time and date in a yyyy:MM:dd:HH:mm:ss format to avoid overwritting files. 
A toast message is also displayed to tell the user if the operation was succesful or not. 
As this example implements scoped storage, if the app is uninstalled the files will be deleted.

Depending on your device, you may find the files in:
sdcard>Android>data>**app package name**>Files>PDF_files, 
storage>self>primary>Android>data>**app package name**>Files>PDF_files, OR
storage>emulated>Android>data>**app package name**>Files>PDF_files (for emulators, though my emulated devices included the files in the above directories as well)
