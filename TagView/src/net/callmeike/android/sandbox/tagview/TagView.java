/* $Id: $
   Copyright 2013, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.callmeike.android.sandbox.tagview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class TagView extends View {
    private static final String TAG = "TAGVIEW";

    private static final String DEFAULT_FONT = "fonts/DroidSansFallback.ttf";

    private static class Tag {
        final int level;
        final String tag;
        String shortTag;
        public Tag(String tag, int level) {
            this.level = level;
            this.tag = tag;
            this.shortTag = tag;
        }
    }

    private final List<Tag> tags = new ArrayList<Tag>();

    private final LevelListDrawable tagBg;
    private final int tagMargin;
    private final int tagPaddingH;
    private final int tagPaddingV;
    private final float tagHeight;
    private final int tagBorderH;

    private final TextPaint textPaint;
    private final float textBaseline;
    private final float textHeight;

    private final Rect bounds = new Rect();
    private final Rect tagRect = new Rect();
    private final RectF tagRectF = new RectF();
    private final PointF tagBorderTL = new PointF();
    private final PointF tagTL = new PointF();

    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Drawable tagDrawable = null;
        int margin = 4;
        int paddingH = 4;
        int paddingV = 4;
        int textColor = Color.BLACK;
        int textSize = 18;
        int textStyle = 0;
        int textFace = 4;

        TypedArray atts = context.obtainStyledAttributes(attrs, R.styleable.tag_view, defStyle, 0);
        try {
            final int n = atts.getIndexCount();
            for (int i = 0; i < n; i++) {
                try {
                    int attr = atts.getIndex(i);
                    switch (attr) {
                        case R.styleable.tag_view_tag_view_tag_drawable:
                            tagDrawable = atts.getDrawable(attr);
                            break;

                        case R.styleable.tag_view_tag_view_tag_margin:
                            margin = atts.getDimensionPixelSize(attr, margin);
                            break;

                        case R.styleable.tag_view_tag_view_tag_padding_horizontal:
                            paddingH = atts.getDimensionPixelSize(attr, paddingH);
                            break;

                        case R.styleable.tag_view_tag_view_tag_padding_vertical:
                            paddingV = atts.getDimensionPixelSize(attr, paddingV);
                            break;

                        case R.styleable.tag_view_tag_view_text_color:
                            textColor = atts.getColor(attr, textColor);
                            break;

                        case R.styleable.tag_view_tag_view_text_size:
                            textSize = atts.getDimensionPixelSize(attr, textSize);
                            break;

                        case R.styleable.tag_view_tag_view_text_style:
                            textStyle = atts.getInt(attr, textStyle);
                            break;

                        case R.styleable.tag_view_tag_view_text_face:
                            textFace = atts.getInt(attr, textFace);
                            break;
                    }
                }
                catch (UnsupportedOperationException e) {
                    Log.w(TAG, "Failed parsing attribute: " + atts.getString(i), e);
                }
                catch (NotFoundException e) {
                    Log.w(TAG, "Failed parsing attribute: " + atts.getString(i), e);
                }
            }
        }
        finally {
            atts.recycle();
        }

        tagMargin = margin;
        tagPaddingH = paddingH;
        tagPaddingV = paddingV;

        textPaint = setTextAppearance(textColor, textSize, textStyle, textFace);
        if ((null != tagDrawable) && (tagDrawable instanceof LevelListDrawable)) {
            tagBg = (LevelListDrawable) tagDrawable;
        }
        else {
            tagBg = new LevelListDrawable();
            tagBg.addLevel(0, 1, new ColorDrawable(Color.WHITE));
        }

        FontMetrics metrics = textPaint.getFontMetrics();
        float baseline = metrics.leading - metrics.ascent;

        textHeight = metrics.descent + baseline;
        textBaseline = tagPaddingV + baseline + 1;

        tagHeight = textHeight + (2 * tagPaddingV);
        tagBorderH = 2 * (tagPaddingH + tagMargin);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context) {
        this(context, null);
    }

    // the requestLayout is necessary; not sure about the invalidate.
    public void addTag(String tg, int level) {
        tags.add(new Tag(tg, level));
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int w = getDefaultSize(getSuggestedMinimumWidth(), wSpec);
        int h = getDefaultSize(getSuggestedMinimumHeight(), hSpec);

        computeBounds(w, h, bounds);

        float tw = 0;
        tagBorderTL.set(bounds.left, bounds.top);
        for (Tag tag: tags) {
            tag.shortTag = tag.tag;
            tw = positionTag(bounds, tagBorderTL, tw, tag);
        }

        h = Math.max(h, Math.round(tagBorderTL.y + tagHeight + tagMargin));

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        computeBounds(getWidth(), getHeight(), bounds);

        float tw = 0;
        tagBorderTL.set(bounds.left, bounds.top);
        for (Tag tag: tags) {
            tw = positionTag(bounds, tagBorderTL, tw, tag);

            tagTL.set(tagBorderTL);
            tagTL.offset(tagMargin, tagMargin);

            tagRectF.set(tagTL.x, tagTL.y, tagTL.x + tw - (2 * tagMargin), tagTL.y + tagHeight);
            tagRectF.round(tagRect);
            tagBg.setBounds(tagRect);
            tagBg.setLevel(tag.level);
            tagBg.draw(canvas);

            canvas.drawText(tag.shortTag, tagTL.x + tagPaddingH, tagTL.y + textBaseline, textPaint);
        }
    }

    private float positionTag(Rect viewBounds, PointF tagLoc, float tw, Tag tag) {
        int maxW = (viewBounds.right - viewBounds.left);
        tagLoc.x += tw;

        while (true) {
            tw = textPaint.measureText(tag.shortTag) + tagBorderH;

            if (tw <= maxW) { break; }

            tag.shortTag
                = TextUtils.ellipsize(tag.shortTag, textPaint, maxW - tagBorderH, TruncateAt.END)
                .toString();
        }

        if (tagLoc.x + tw > maxW) {
            tagLoc.set(viewBounds.left, tagLoc.y + tagHeight + (2 * tagMargin));
        }

        return tw;
    }

    // stolen pretty much directly from TextView
    private TextPaint setTextAppearance(int color, int size, int style, int face) {
        TextPaint paint = new TextPaint();

        Typeface tf = null;
        switch (face) {
            case 1:
                tf = Typeface.SANS_SERIF;
                break;
            case 2:
                tf = Typeface.SERIF;
                break;
            case 3:
                tf = Typeface.MONOSPACE;
                break;
            case 4:
                try { tf = Typeface.createFromAsset(getContext().getAssets(), DEFAULT_FONT); }
                catch (Exception e) { Log.w(TAG, "Could not create default font"); }
                break;
        }

        if (0 >= style) {
            paint.setFakeBoldText(false);
            paint.setTextSkewX(0);
            paint.setTypeface(tf);
        }
        else {
            tf = (tf != null)
                ? Typeface.create(tf, style)
                : Typeface.defaultFromStyle(style);
            paint.setTypeface(tf);

            // now compute what (if any) algorithmic styling is needed
            int typefaceStyle = tf != null ? tf.getStyle() : 0;
            int need = style & ~typefaceStyle;
            paint.setFakeBoldText((need & Typeface.BOLD) != 0);
            paint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
        }

        paint.setAntiAlias(true);
        paint.setTextSize(size);
        paint.setColor(color);

        return paint;
    }

    private void computeBounds(int w, int h, Rect r) {
        int padL = getLeftPaddingOffset();
        int padT = getTopPaddingOffset();
        bounds.set(
                padL,
                padT,
                w - (padL + getRightPaddingOffset()),
                h - (padT + getBottomPaddingOffset()));
    }
}
