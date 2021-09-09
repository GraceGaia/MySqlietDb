package com.grace.mysqlietdatabase

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import java.util.jar.Attributes

class MainActivity : AppCompatActivity() {
    var editTextName:EditText? = null
    var editTextEmail:EditText? = null
    var editTextIdNumber:EditText? = null
    var buttonSave:Button? = null
    var buttonView:Button? = null
    var buttonDelete:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextName = findViewById(R.id.mEditName)
        editTextEmail = findViewById(R.id.mEditEmail)
        editTextIdNumber = findViewById(R.id.mEditNumber)
        buttonSave = findViewById(R.id.mBtnSave)
        buttonView = findViewById(R.id.mBtnView)
        buttonDelete = findViewById(R.id.mBtnDelete)

        //Create a database
        var db:SQLiteDatabase = openOrCreateDatabase("registrations", Context.MODE_PRIVATE,
                                                                                     null)
        //Create a table called users in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")

        buttonSave!!.setOnClickListener {
            var name = editTextName!!.text.toString()
            var email = editTextEmail!!.text.toString()
            var idNumber = editTextIdNumber!!.text.toString()
            //Check if the user is submitting empty fields
            if (name.isEmpty() or email.isEmpty() or idNumber.isEmpty()){
                message("EMPTY FIELDS","Please fill all the input fields")
            }else{
                //Proceed to save data
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idNumber+"')")
                message("SUCCESS!!!","Record saved successfully")
                clear()
            }
        }
        buttonView!!.setOnClickListener {
            //Use the cursor to select the saved records
            var cursor = db.rawQuery("SELECT * FROM users",null)
            if (cursor.count == 0){
                message("EMPTY DB","Sorry, we didn't find any records")
            }else{
                //Use buffer to append records from db
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    buffer.append(cursor.getString(0)+"\n")
                    buffer.append(cursor.getString(1)+"\n")
                    buffer.append(cursor.getString(2)+"\n\n")
                }
                message("DB RECORDS",buffer.toString())
            }
        }
        buttonDelete!!.setOnClickListener {
            //Get the user ID so you can use it to delete the owner
            var idNumber = editTextIdNumber!!.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELD","Please enter ID number")
            }else{
                //Use the cursor to select the user with the given ID
                var cursor = db.rawQuery(
                    "SELECT * FROM users WHERE kitambulisho = '"+idNumber+"'",null)
                //Check if the record is available in the DB
                if (cursor.count == 0){
                    message("NO SUCH RECORD","Sorry, no record found")
                }else{
                    //Finally delete the record
                    db.execSQL("DELETE FROM users WHERE kitambulisho = '"+idNumber+"'")
                    message("SUCCESS!!!","Record deleted successfully")
                    clear()
                }
            }
        }
    }
    //This function is to clear fields to allow for next entry
    private fun clear(){
        editTextName!!.setText(null)
        editTextEmail!!.setText(null)
        editTextIdNumber!!.setText(null)
    }

    //This method is responsible for displaying all the messages
    //This is how to do an alert dialog on android
    private fun message(title:String, message:String){
        var alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Ok",DialogInterface.OnClickListener { dialogInterface, i ->  })
        alert.create().show()
    }
}