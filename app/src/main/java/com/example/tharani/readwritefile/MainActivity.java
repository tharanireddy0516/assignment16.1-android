package com.example.tharani.readwritefile;
/*import is libraries imported for writing the code
* AppCompatActivity is base class for activities
* Bundle handles the orientation of the activity
*/

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    /*onCreate is the first method in the life cycle of an activity
       savedInstance passes data to super class,data is pull to store state of application
     * setContentView is used to set layout for the activity
     *R is a resource and it is auto generate file
     * activity_main assign an integer value*/
    //declaring variables
    EditText editText;
    Button button1;
    TextView textView;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //implementing  UI components
        button1 =  findViewById(R.id.btn_add);
        button2 =  findViewById(R.id.btn_delete);
        editText =  findViewById(R.id.enter_data);
        textView =  findViewById(R.id.show_data);


        //here it is to ask permission for the Write external storage to allow or denny them
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//if statement decides whether this statement will get execute or not

            requestPermissions(permissions, 1);//requests Permissions
        }
      //then in case the permission is allow which is one the layout file which is from .xml file will be shown
        //by  these set onClickListener if we click ok add data button it adds them
        //if we click on delete button it shows the file is deleted or else the file not found as a toast
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Mytask().execute();//here task will get executes

            }
        });
        //setting onClickListener for button2
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletefile();//the file gets deleted
            }
        });
    }
    @Override
//here it is to ask for the runtime permissions  to allow or denny them
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {//switch is a  multi way branch statement,dispatch execution to diff parts of code
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    /*Toast is a pop up message or  a view containing a quick little message
                    LENGTH_SHORT display in short period of time*/
                    break;
                }
        }
    }

    //here creating a method called write external storage
    public void writeexternalstorage(){
        //where taking a string type variable state
        String state ;//here string is a sequence of characters

        //to check whether the mounted the External storage state or to created the it
        state = Environment.getExternalStorageState();
        Log.d("SDCARD",state);//showing that a mount is there
        Toast.makeText(this, ""+state, Toast.LENGTH_SHORT).show();
        /*Toast is a pop up message or  a view containing a quick little message
             LENGTH_SHORT display in short period of time*/
        //if the media mounted is equals to state
        if(Environment.MEDIA_MOUNTED.equals(state)){
            //then file directory will be created, creating a path for the Myapp file
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File (Root.getAbsolutePath()+"/MyAppFile");
            if(!Dir.exists()){ //if it does not exits to make dir in the virtual device
                Dir.mkdir();
            }
            //now creating file object for the dir called MyMessage.txt
            File file  = new File(Dir,"MyMessage.txt");
            //now get the edittext to the message
            String message = editText.getText().toString();
            try {//taking try block
                //now creating fileoutputstream object for the file to write the text in the edit text
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PrintWriter writer = new PrintWriter(fileOutputStream);
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
                //it sets to edit text
                editText.setText("");
                Toast.makeText(this, "Message Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {//if their is any error in try block catch block catches the error
                //FileNotFoundException is a compile time exceptions gets when file was not found
                e.printStackTrace();//printStackTrace is used to print the description of exception
            } catch (IOException e) {//catches IOException
                e.printStackTrace();
            }
        }
        else{//it the external storage not exits it shows the toast
            Toast.makeText(this, "External Storage Not Mounted", Toast.LENGTH_SHORT).show();
        }

    }



    //here taking read external storage method
    public void readexternalstoragee(){
        //creating an dir tfor the file to read the text using file input stream
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File (Root.getAbsolutePath()+"/MyAppFile");
        File file  = new File(Dir,"MyMessage.txt");
        String message;
        try {
            FileInputStream fileinputstream = new FileInputStream(file);
            InputStreamReader inputstreamreader = new InputStreamReader(fileinputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            StringBuffer stringbuffer = new StringBuffer();
            while((message = bufferedreader.readLine())!=null){
                stringbuffer.append(message+"\n");

            }
            textView.setText(stringbuffer.toString());
            textView.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //this method is to delete file from the My app dir of My message.txt file
    public void deletefile(){
        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File (Root.getAbsolutePath()+"/MyAppFile");
        File file  = new File(Dir,"MyMessage.txt");
        if(Dir.exists()){//if dir exits on clicking the delete button it will be showing the the toast
            boolean deleted = file.delete();

            Toast.makeText(this, "File Deleted "+deleted, Toast.LENGTH_SHORT).show();
             /*Toast is a pop up message or  a view containing a quick little message
             LENGTH_SHORT display in short period of time*/
        }
        else{//or else it displays the following toast
            Toast.makeText(this, "File Not found", Toast.LENGTH_SHORT).show();
             /*Toast is a pop up message or  a view containing a quick little message
             LENGTH_SHORT display in short period of time
             displays File Not found*/
        }

    }//implementing the methods in the async task to run the methods in the doing back ground method
    public class Mytask extends AsyncTask<Void,Void,String> {

        //this method runs on UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //this method run in separate thread
        @Override
        protected String doInBackground(Void... voids) {

            //do task getting information from web
            return null;//return null
        }


        //run on UI thread
        @Override
        protected void onPostExecute(String o) {
            //do something with data like update data
            super.onPostExecute(o);//The super keyword in java is a reference variable which is used to refer immediate parent class objec
            writeexternalstorage();//writes external stoage
            readexternalstoragee();//reads
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
          /*Toast is a pop up message or  a view containing a quick little message
             LENGTH_SHORT display in short period of time*/
        }

    }
}



