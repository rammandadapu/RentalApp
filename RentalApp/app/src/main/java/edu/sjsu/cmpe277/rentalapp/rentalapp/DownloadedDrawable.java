package edu.sjsu.cmpe277.rentalapp.rentalapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by divya.chittimalla on 3/24/16.
 */
public class DownloadedDrawable extends ColorDrawable {
    private final WeakReference<ImageDownloaderTask> bitmapDownloaderTaskReference;

    public DownloadedDrawable(ImageDownloaderTask bitmapDownloaderTask) {
        super(Color.TRANSPARENT);
        bitmapDownloaderTaskReference =
                new WeakReference<ImageDownloaderTask>(bitmapDownloaderTask);
    }

    public ImageDownloaderTask getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}