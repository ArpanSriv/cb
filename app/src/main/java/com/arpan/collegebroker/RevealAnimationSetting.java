package com.arpan.collegebroker;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RevealAnimationSetting implements Parcelable {
    public abstract int getCenterX();

    public abstract int getCenterY();

    public abstract int getWidth();

    public abstract int getHeight();

    public static RevealAnimationSetting create(int centerX, int centerY, int width, int height) {
        return new RevealAnimationSetting() {
            @Override
            public int getCenterX() {
                return centerX;
            }

            @Override
            public int getCenterY() {
                return centerY;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(centerX);
                dest.writeInt(centerY);
                dest.writeInt(width);
                dest.writeInt(height);
            }
        };
    }
}