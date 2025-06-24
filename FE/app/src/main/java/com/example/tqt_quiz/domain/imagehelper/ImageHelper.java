package com.example.tqt_quiz.domain.imagehelper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageHelper
{
    public static MultipartBody.Part uriToPart(Context context, Uri uri, String formName) throws IOException
    {
        ContentResolver contentResolver = context.getContentResolver();
        String mime = contentResolver.getType(uri);
        String fileName = queryName(contentResolver, uri);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(InputStream is = contentResolver.openInputStream(uri))
        {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1)
            {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        }

        RequestBody body = RequestBody.create(
                byteArrayOutputStream.toByteArray(),
                mime != null ? MediaType.parse(mime) : MediaType.parse("image/*")
        );

        return MultipartBody.Part.createFormData(formName, fileName, body);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        String name = "upload";
        Cursor c = resolver.query(uri, null, null, null, null);
        if (c != null) {
            int nameIdx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIdx >= 0 && c.moveToFirst()) name = c.getString(nameIdx);
            c.close();
        }
        return name;
    }
}
