package com.vau.myappmanager.utils;

import android.content.Context;
import android.os.Environment;

import com.vau.myappmanager.data.AppRepository;

import java.io.File;

public class CopyFile {
    public static File getAbsoluteDir(Context ctx, AppRepository appRepository) {
        return new File(Environment.getExternalStorageDirectory(), appRepository.getExternalFile(ctx));
    }
}
