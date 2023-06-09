package com.example.proyecto.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.proyecto.R;
import com.example.proyecto.databinding.AdapterContactosBinding;

public class ContactosAdapter extends CursorAdapter {
    public final static String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.PHOTO_URI,
    };

    public static final String FILTER = ContactsContract.Contacts.DISPLAY_NAME + " NOT LIKE '%@%'";

    public static final String ORDER = String.format("%1$s COLLATE NOCASE", ContactsContract.Contacts.DISPLAY_NAME);

    public ContactosAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        AdapterContactosBinding binding = AdapterContactosBinding.inflate(LayoutInflater.from(context), parent, false);
        return binding.getRoot();
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AdapterContactosBinding binding = AdapterContactosBinding.bind(view);
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        //Set the contact name
        binding.contactName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
        //Set the contact is favorite
        if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.STARRED)) == 1)
            binding.contactFavorite.setColorFilter(ContextCompat.getColor(context, R.color.amber_400), android.graphics.PorterDuff.Mode.SRC_IN);
        //set the contact photo
        if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)) != null)
            binding.contactImage.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))));
        else {
            Drawable noPhotoICon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.baseline_tag_faces_24, null);
           // noPhotoICon.setTint(ContextCompat.getColor(context, R.color.light_blue_400));
            binding.contactImage.setImageDrawable(noPhotoICon);
        }
        //Get phone number based in contact id
        Cursor phonesQuery = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
        if(phonesQuery.getCount() > 0) {
            phonesQuery.moveToFirst();
            String phoneNumber = phonesQuery.getString(phonesQuery.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            binding.contactPhone.setText(phoneNumber);
            binding.contactPhone.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(String.format("tel:%s", phoneNumber)));
                context.startActivity(intent);
            });
            phonesQuery.close();
        }
        //Get email based in contact id
        Cursor emailsQuery = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
        if(emailsQuery.getCount() > 0) {
            emailsQuery.moveToFirst();
            String email = emailsQuery.getString(emailsQuery.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            binding.contactEmail.setText(email);
            emailsQuery.close();
        }
    }
}
