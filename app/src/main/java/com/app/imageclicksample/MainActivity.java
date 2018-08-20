package com.app.imageclicksample;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivBlank = (ImageView) findViewById(R.id.iv_blank);
        if (ivBlank != null) {
            ivBlank.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean handledHere = false;

        final int action = motionEvent.getAction();

        final int evX = (int) motionEvent.getX();
        final int evY = (int) motionEvent.getY();
//        int nextImage = -1;            // resource id of the next image to display

        // If we cannot find the imageView, return.
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_blank);
        if (imageView == null) return false;

        // When the action is Down, see if we should show the "pressed" image for the default image.
        // We do this when the default image is showing. That condition is detectable by looking at the
        // tag of the view. If it is null or contains the resource number of the default image, display the pressed image.
        Integer tagNum = (Integer) imageView.getTag();
        int currentResource = (tagNum == null) ? R.drawable.map_blank : tagNum.intValue();

        // Now that we know the current resource being displayed we can handle the DOWN and UP events.

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (currentResource == R.drawable.map_blank) {
//                    nextImage = R.drawable.map_filled;
                    handledHere = true;
       /*
       } else if (currentResource != R.drawable.p2_ship_default) {
         nextImage = R.drawable.p2_ship_default;
         handledHere = true;
       */
                } else handledHere = true;
                break;

            case MotionEvent.ACTION_UP:
                // On the UP, we do the click action.
                // The hidden image (image_areas) has three different hotspots on it.
                // The colors are red, blue, and yellow.
                // Use image_areas to determine which region the user touched.
                int touchColor = getHotspotColor(R.id.iv_filled, evX, evY);

                // Compare the touchColor to the expected values. Switch to a different image, depending on what color was touched.
                // Note that we use a Color Tool object to test whether the observed color is close enough to the real color to
                // count as a match. We do this because colors on the screen do not match the map exactly because of scaling and
                // varying pixel density.
                ColorTool ct = new ColorTool();
                int tolerance = 25;
//                nextImage = R.drawable.map_filled;
                if (ct.closeMatch(ContextCompat.getColor(this, R.color.manipur), touchColor, tolerance)) {
//                    nextImage = R.drawable.map_filled;
                    showOkDialog("Manipur", this);
                } else if (ct.closeMatch(ContextCompat.getColor(this, R.color.haryana), touchColor, tolerance)) {
//                    nextImage = R.drawable.map_filled;
                    showOkDialog("Haryana", this);
                } else if (ct.closeMatch(ContextCompat.getColor(this, R.color.punjab), touchColor, tolerance)) {
//                    nextImage = R.drawable.map_filled;
                    showOkDialog("Punjab", this);
                } else if (ct.closeMatch(ContextCompat.getColor(this, R.color.rajsthan), touchColor, tolerance)) {
//                    nextImage = R.drawable.map_filled;
                    showOkDialog("Rajsthan", this);

                } else {
                    toast("Only Punjab, Haryana, Rajsthan and Manipur can be identified");
                }

                // If the next image is the same as the last image, go back to the default.
                // toast ("Current image: " + currentResource + " next: " + nextImage);
//                if (currentResource == nextImage) {
//                    nextImage = R.drawable.map_blank;
//                }
                handledHere = true;
                break;

            default:
                handledHere = false;
        } // end switch

        if (handledHere) {
/*
            if (nextImage > 0) {
                imageView.setImageResource(nextImage);
                imageView.setTag(nextImage);
            }*/
        }
        return handledHere;
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Get the color from the hotspot image at point x-y.
     */

    public int getHotspotColor(int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById(hotspotId);
        if (img == null) {
            Log.d("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }

    public static final void showOkDialog(String dlgText, final Context context) {
        if (context == null) {
            return;
        }

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(dlgText);

        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alertDialogBuilder.show();
    }
}
