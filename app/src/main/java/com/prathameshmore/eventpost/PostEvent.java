package com.prathameshmore.eventpost;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class PostEvent extends AppCompatActivity {

    private ImageButton selectImage;
    private static final int GALLERY_REQUEST = 1;
    Uri imageUri;

    private EditText mPostTitle;
    private EditText mPostDesc;
    private EditText mPostBranch;
    private EditText mPostDate;
    private EditText mPostTime;
    private EditText mPostContact;
    private EditText mPostMail;
    private EditText mPostAddress;

    private int year, month, day;
    private Calendar calendar;
    String date;

    private Button btnPost;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year,month + 1,day);

        selectImage = findViewById(R.id.btn_image);

        mPostTitle = findViewById(R.id.et_name);
        mPostDesc = findViewById(R.id.et_desc);
        mPostBranch = findViewById(R.id.et_branch);
        mPostDate = findViewById(R.id.et_date);
        mPostTime = findViewById(R.id.et_time);
        mPostContact = findViewById(R.id.et_contact);
        mPostMail = findViewById(R.id.et_mail);
        mPostAddress = findViewById(R.id.et_address);
        btnPost = findViewById(R.id.btn_post);

        mProgressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        mStorage = FirebaseStorage.getInstance().getReference();

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,GALLERY_REQUEST);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //startPosting();
                startNewPost();
            }
        });
    }

    private void showDate(int year, int month, int day) {
        date = String.valueOf(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    public void setDate(View view) {
        showDialog(999);
        mPostDate.setText(date);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void startNewPost() {

        mProgressDialog.setMessage("Posting");
        mProgressDialog.show();
        final String name = mPostTitle.getText().toString().trim();
        final String desc = mPostDesc.getText().toString().trim();
        final String branch = mPostBranch.getText().toString().trim();
        final String time = mPostTime.getText().toString().trim();
        final String contact = mPostContact.getText().toString().trim();
        final String mail = mPostMail.getText().toString().trim();
        final String address = mPostAddress.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(branch) && /*!TextUtils.isEmpty(date) && */!TextUtils.isEmpty(time) && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(address)) {
            DatabaseReference newPost = mDatabase.push();
            newPost.child("name").setValue(name);
            newPost.child("desc").setValue(desc);
            newPost.child("branch").setValue(branch);
            newPost.child("date").setValue(date);
            newPost.child("time").setValue(time);
            newPost.child("contact").setValue(contact);
            newPost.child("mail").setValue(mail);
            newPost.child("address").setValue(address);
            mPostTitle.setText("");
            mPostDesc.setText("");
            mPostBranch.setText("");
            mPostDate.setText("");
            mPostTime.setText("");
            mPostContact.setText("");
            mPostMail.setText("");
            mPostAddress.setText("");
            Toast.makeText(this, "Event is posted successfully", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
    }




    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();

            StorageReference mStorageReference = mStorage.child("Photos").child(imageUri.getLastPathSegment());

            mStorageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostEvent.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            });

        } */
}
