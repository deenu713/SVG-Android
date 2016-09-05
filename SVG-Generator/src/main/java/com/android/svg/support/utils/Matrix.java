package com.android.svg.support.utils;

import java.io.PrintWriter;

/**
 * The Matrix class holds a 3x3 matrix for transforming coordinates.
 */
public class Matrix {

    public static final int MSCALE_X = 0;   //!< use with getValues/setValues
    public static final int MSKEW_X  = 1;   //!< use with getValues/setValues
    public static final int MTRANS_X = 2;   //!< use with getValues/setValues
    public static final int MSKEW_Y  = 3;   //!< use with getValues/setValues
    public static final int MSCALE_Y = 4;   //!< use with getValues/setValues
    public static final int MTRANS_Y = 5;   //!< use with getValues/setValues
    public static final int MPERSP_0 = 6;   //!< use with getValues/setValues
    public static final int MPERSP_1 = 7;   //!< use with getValues/setValues
    public static final int MPERSP_2 = 8;   //!< use with getValues/setValues

    private final float[] MATRIX = new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1};

    /**
     * (deep) copy the src matrix into this matrix. If src is null, reset this
     * matrix to the identity matrix.
     */
    public void set(Matrix src) {
        if (src == null) {
            reset();
        } else {
            float[] values = new float[9];
            src.getValues(values);
            setValues(values);
        }
    }

    /** Set the matrix to identity */
    public void reset() {
        MATRIX[MSCALE_X] = 1;
        MATRIX[MSKEW_X] = 0;
        MATRIX[MTRANS_X] = 0;
        MATRIX[MSKEW_Y] = 1;
        MATRIX[MSCALE_Y] = 0;
        MATRIX[MTRANS_Y] = 0;
        MATRIX[MPERSP_0] = 0;
        MATRIX[MPERSP_1] = 0;
        MATRIX[MPERSP_2] = 1;
    }

    /**
     * Preconcats the matrix with the specified matrix.
     * M' = M * other
     */
    public boolean preConcat(Matrix other) {
        float[] otherValue = new float[9];
        other.getValues(otherValue);
        float[] newValue = new float[9];
        newValue[MSCALE_X] = MATRIX[MSCALE_X] * otherValue[MSCALE_X] + MATRIX[MSKEW_X] * otherValue[MSKEW_Y]
                + MATRIX[MTRANS_X] * otherValue[MPERSP_0];
        newValue[MSKEW_X] = MATRIX[MSCALE_X] * otherValue[MSKEW_X] + MATRIX[MSKEW_X] * otherValue[MSCALE_Y]
                + MATRIX[MTRANS_X] * otherValue[MPERSP_1];
        newValue[MTRANS_X] = MATRIX[MSCALE_X] * otherValue[MTRANS_X] + MATRIX[MSKEW_X] * otherValue[MTRANS_Y]
                + MATRIX[MTRANS_X] * otherValue[MPERSP_2];
        newValue[MSKEW_Y] = MATRIX[MSKEW_Y] * otherValue[MSCALE_X] + MATRIX[MSCALE_Y] * otherValue[MSKEW_Y]
                + MATRIX[MTRANS_Y] * otherValue[MPERSP_0];
        newValue[MSCALE_Y] = MATRIX[MSKEW_Y] * otherValue[MSKEW_X] + MATRIX[MSCALE_Y] * otherValue[MSCALE_Y]
                + MATRIX[MTRANS_Y] * otherValue[MPERSP_1];
        newValue[MTRANS_Y] = MATRIX[MSKEW_Y] * otherValue[MTRANS_X] + MATRIX[MSCALE_Y] * otherValue[MTRANS_Y]
                + MATRIX[MTRANS_Y] * otherValue[MPERSP_2];
        newValue[MPERSP_0] = MATRIX[MPERSP_0] * otherValue[MSCALE_X] + MATRIX[MPERSP_1] * otherValue[MSKEW_Y]
                + MATRIX[MPERSP_2] * otherValue[MPERSP_0];
        newValue[MPERSP_1] = MATRIX[MPERSP_0] * otherValue[MSKEW_X] + MATRIX[MPERSP_1] * otherValue[MSCALE_Y]
                + MATRIX[MPERSP_2] * otherValue[MPERSP_1];
        newValue[MPERSP_2] = MATRIX[MPERSP_0] * otherValue[MTRANS_X] + MATRIX[MPERSP_1] * otherValue[MTRANS_Y]
                + MATRIX[MPERSP_2] * otherValue[MPERSP_2];
        setValues(newValue);
        return true;
    }

    /**
     * Postconcats the matrix with the specified translation.
     * M' = T(dx, dy) * M
     */
    public boolean postTranslate(float dx, float dy) {
        Matrix matrix = new Matrix();
        matrix.setValues(new float[] {1, 0, dx, 0, 1, dy, 0, 0, 1});
        Matrix current = new Matrix();
        current.setValues(MATRIX);
        matrix.preConcat(current);
        set(matrix);
        return true;
    }

    /**
     * Postconcats the matrix with the specified scale.
     * M' = S(sx, sy) * M
     */
    public boolean postScale(float sx, float sy) {
        Matrix matrix = new Matrix();
        matrix.setValues(new float[] {sx, 0, 0, 0, sy, 0, 0, 0, 1});
        Matrix current = new Matrix();
        current.setValues(MATRIX);
        matrix.preConcat(current);
        set(matrix);
        return true;
    }

    /**
     * Postconcats the matrix with the specified rotation.
     * M' = R(degrees, px, py) * M
     */
    public boolean postRotate(float degrees, float px, float py) {
        double radians = Math.toRadians(degrees);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        Matrix matrix = new Matrix();
        matrix.setValues(new float[] {cos, -sin, - px * cos + py * sin + px, sin, cos, - px * sin - py * cos + py, 0, 0, 1});
        Matrix current = new Matrix();
        current.setValues(MATRIX);
        matrix.preConcat(current);
        set(matrix);
        return true;
    }

    /** Copy 9 values from the matrix into the array.
     */
    public void getValues(float[] values) {
        if (values.length < 9) {
            throw new ArrayIndexOutOfBoundsException();
        }
        values[MSCALE_X] = MATRIX[MSCALE_X];
        values[MSKEW_X] = MATRIX[MSKEW_X];
        values[MTRANS_X] = MATRIX[MTRANS_X];
        values[MSKEW_Y] = MATRIX[MSKEW_Y];
        values[MSCALE_Y] = MATRIX[MSCALE_Y];
        values[MTRANS_Y] = MATRIX[MTRANS_Y];
        values[MPERSP_0] = MATRIX[MPERSP_0];
        values[MPERSP_1] = MATRIX[MPERSP_1];
        values[MPERSP_2] = MATRIX[MPERSP_2];
    }

    /** Copy 9 values from the array into the matrix.
     Depending on the implementation of Matrix, these may be
     transformed into 16.16 integers in the Matrix, such that
     a subsequent call to getValues() will not yield exactly
     the same values.
     */
    public void setValues(float[] values) {
        if (values.length < 9) {
            throw new ArrayIndexOutOfBoundsException();
        }
        MATRIX[MSCALE_X] = values[MSCALE_X];
        MATRIX[MSKEW_X] = values[MSKEW_X];
        MATRIX[MTRANS_X] = values[MTRANS_X];
        MATRIX[MSKEW_Y] = values[MSKEW_Y];
        MATRIX[MSCALE_Y] = values[MSCALE_Y];
        MATRIX[MTRANS_Y] = values[MTRANS_Y];
        MATRIX[MPERSP_0] = values[MPERSP_0];
        MATRIX[MPERSP_1] = values[MPERSP_1];
        MATRIX[MPERSP_2] = values[MPERSP_2];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Matrix{");
        toShortString(sb);
        sb.append('}');
        return sb.toString();

    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder(64);
        toShortString(sb);
        return sb.toString();
    }

    public void toShortString(StringBuilder sb) {
        float[] values = new float[9];
        getValues(values);
        sb.append('[');
        sb.append(values[0]); sb.append(", "); sb.append(values[1]); sb.append(", ");
        sb.append(values[2]); sb.append("][");
        sb.append(values[3]); sb.append(", "); sb.append(values[4]); sb.append(", ");
        sb.append(values[5]); sb.append("][");
        sb.append(values[6]); sb.append(", "); sb.append(values[7]); sb.append(", ");
        sb.append(values[8]); sb.append(']');
    }

    public void printShortString(PrintWriter pw) {
        float[] values = new float[9];
        getValues(values);
        pw.print('[');
        pw.print(values[0]); pw.print(", "); pw.print(values[1]); pw.print(", ");
        pw.print(values[2]); pw.print("][");
        pw.print(values[3]); pw.print(", "); pw.print(values[4]); pw.print(", ");
        pw.print(values[5]); pw.print("][");
        pw.print(values[6]); pw.print(", "); pw.print(values[7]); pw.print(", ");
        pw.print(values[8]); pw.print(']');
    }
}
