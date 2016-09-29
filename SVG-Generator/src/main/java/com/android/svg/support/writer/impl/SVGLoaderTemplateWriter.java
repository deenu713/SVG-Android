package com.android.svg.support.writer.impl;


import com.android.svg.support.Config;
import com.android.svg.support.writer.JavaClassWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGLoaderTemplateWriter extends JavaClassWriter {

    private List<String> mDrawableRendererList = new ArrayList<>();

    public SVGLoaderTemplateWriter() {
        setClassSimpleName("SVGLoader");
    }

    public void addRendererName(String name) {
        mDrawableRendererList.add(name);
    }

    @Override
    protected void writeImports(BufferedWriter bw) throws IOException {
        super.writeImports(bw);
        bw.newLine();
        bw.write("import " + Config.APP_PACKAGE + ".R;");
        bw.newLine();
        bw.newLine();
        bw.write("import android.content.Context;");
        bw.newLine();
        bw.write("import android.graphics.drawable.Drawable;");
        bw.newLine();
        bw.write("import android.util.LongSparseArray;");
        bw.newLine();
        bw.newLine();
        bw.write("import com.android.svg.support.SVGDrawable;");
        bw.newLine();
        bw.write("import com.android.svg.support.SVGHelper;");
        bw.newLine();
        bw.newLine();
    }

    @Override
    protected void writeFields(BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.write(HEAD_SPACE + "private static LongSparseArray<Drawable.ConstantState> sPreloadedDrawables;");
        bw.newLine();
    }

    @Override
    protected void writeClassComment(BufferedWriter bw) throws IOException {
        super.writeClassComment(bw);
        bw.write("/**");
        bw.newLine();
        bw.write(" * AUTO-GENERATED FILE.  DO NOT MODIFY.");
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * This class was automatically generated by the");
        bw.newLine();
        bw.write(" * SVG-Generator. It should not be modified by hand.<br><br>");
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" * Call the follow in your Application: ");
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" * <pre class=\"prettyprint\">");
        bw.newLine();
        bw.write(" * public class MyApplication extends Application {");
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" *    public void onCreate() {");
        bw.newLine();
        bw.write(" *        SVGLoader.load(this)");
        bw.newLine();
        bw.write(" *    }");
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" * }");
        bw.newLine();
        bw.write(" * </pre>");
        bw.newLine();
        bw.write(" */");
    }

    @Override
    protected void writeConstructMethods(BufferedWriter bw) throws IOException {
    }

    @Override
    protected void writeMethods(BufferedWriter bw) throws IOException {
        writeLoadMethod(bw);
        writeAddMethod(bw);
    }

    private void writeLoadMethod(BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.write(HEAD_SPACE + "public static void load(Context context) {");
        bw.newLine();
        bw.write(HEAD_SPACE + HEAD_SPACE + "sPreloadedDrawables = SVGHelper.hackPreloadDrawables(context.getResources());");
        bw.newLine();
        bw.write(HEAD_SPACE + HEAD_SPACE + "if (sPreloadedDrawables == null) {");
        bw.newLine();
        bw.write(HEAD_SPACE + HEAD_SPACE + HEAD_SPACE + "return;");
        bw.newLine();
        bw.write(HEAD_SPACE + HEAD_SPACE + "}");
        bw.newLine();
        for (String name : mDrawableRendererList) {
            bw.write(HEAD_SPACE + HEAD_SPACE + "add(context, R.drawable." + name + ", " +
                    "SVGDrawable.SVGDrawableConstantState.create(new " + name + "(context)));");
            bw.newLine();
        }
        bw.write(HEAD_SPACE + "}");
        bw.newLine();
    }

    private void writeAddMethod(BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.write(HEAD_SPACE + "private static void add(Context context, int resId, SVGDrawable.SVGDrawableConstantState state) {");
        bw.newLine();
        bw.write(HEAD_SPACE + HEAD_SPACE + "sPreloadedDrawables.put(SVGHelper.resKey(context, resId), state);");
        bw.newLine();
        bw.write(HEAD_SPACE + "}");
        bw.newLine();
    }
}
