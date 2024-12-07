package com.shop.anandnameplate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_filling_details extends AppCompatActivity {

    private CircleImageView image;
    private TextInputEditText name;
    private Spinner unit_name;
    public Uri imageUri;
    private Button Continue,take_image,choose_image;
    private StorageReference storageReference;
    DatabaseReference reference;
    private String phonenumber,used_Referral;
    private ProgressBar progressBar;
    Query checkuser;
    public static String userImageUrl = "";

    public void chooser(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Your Photo"), 1);
    }

    public void taker(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, 1);
        }
    }

    public void btncontinue(View view)
    {
            if (!(name.getText().toString().isEmpty()))
            {
                if (!(unit_name.getSelectedItem().toString().equals("Select Your Unit Name")))
                {
                    Continue.setEnabled(false);
                    take_image.setEnabled(false);
                    choose_image.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    
                    // Uploading Image
                    storageReference = FirebaseStorage.getInstance().getReference().child("user/"+phonenumber);
                    storageReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Uploading Name and Unit Name
                                    reference.child(phonenumber).child("name").setValue(name.getText().toString());
                                    reference.child(phonenumber).child("unit_name").setValue(unit_name.getSelectedItem().toString());

                                    //Uploading ImageUrl
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            reference.child(phonenumber).child("imageUrl").setValue(imageUrl);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Continue.setEnabled(true);
                                            take_image.setEnabled(true);
                                            choose_image.setEnabled(true);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(user_filling_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent intent = new Intent(user_filling_details.this, refer.class);
                                    intent.putExtra("phonenumber", phonenumber);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Continue.setEnabled(true);
                                    take_image.setEnabled(true);
                                    choose_image.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(user_filling_details.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(this, "Select Your Unit Name", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                name.setError("Name Can't be Empty");
            }
        }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData()!= null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_filling_details);
        hooks();
        unit_name.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, UnitData.unitNames));
        phonenumber = getIntent().getStringExtra("phonenumber");
        used_Referral = getIntent().getStringExtra("used_Referral");
    }

    private void hooks()
    {
        image = findViewById(R.id.paytm_image);
        Continue = findViewById(R.id.Continue);
        take_image = findViewById(R.id.take_image);
        choose_image = findViewById(R.id.choose_image);
        name = findViewById(R.id.name);
        unit_name = findViewById(R.id.unit_name);
        progressBar = findViewById(R.id.progressBar);
        storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}