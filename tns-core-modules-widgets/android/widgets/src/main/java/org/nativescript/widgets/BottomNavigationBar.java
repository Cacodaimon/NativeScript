/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nativescript.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomNavigationBar extends LinearLayout {
    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position}
         *         is selected.
         */
        int getIndicatorColor(int position);

    }

    private static final int BOTTOM_NAV_HEIGHT = 56;
    private static final int ITEM_TEXT_SIZE_SP = 12;
    private static final int ITEM_TEXT_MAX_WIDTH = 144;

    private TabItemSpec[] mTabItems;
    private SparseArray<String> mContentDescriptions = new SparseArray<String>();

    private final TabStrip mTabStrip;

    public BottomNavigationBar(Context context) {
        this(context, null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTabStrip = new TabStrip(context);
        mTabStrip.setSelectedIndicatorColors(0x00FFFFFF);
        int bottomNavHeight = (int) (BOTTOM_NAV_HEIGHT * getResources().getDisplayMetrics().density);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, bottomNavHeight);
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     *
     * If you only require simple customisation then you can use
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
//        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setTabTextColor(int color){
        mTabStrip.setTabTextColor(color);
    }

    public int getTabTextColor(){
        return mTabStrip.getTabTextColor();
    }

    public void setSelectedTabTextColor(int color){
        mTabStrip.setSelectedTabTextColor(color);
    }

    public int getSelectedTabTextColor(){
        return mTabStrip.getSelectedTabTextColor();
    }

    public void setTabTextFontSize(float fontSize){
        mTabStrip.setTabTextFontSize(fontSize);
    }

    public float getTabTextFontSize(){
        return mTabStrip.getTabTextFontSize();
    }

    public void setItems(TabItemSpec[] items) {
        mTabStrip.removeAllViews();
        mTabItems = items;
        populateTabStrip();
    }

    /**
     * Updates the UI of an item at specified index
     */
    public void updateItemAt(int position, TabItemSpec tabItem) {
        LinearLayout ll = (LinearLayout)mTabStrip.getChildAt(position);
        ImageView imgView = (ImageView)ll.getChildAt(0);
        TextView textView = (TextView)ll.getChildAt(1);
        this.setupItem(ll, textView, imgView, tabItem);
    }
  
    /**
     * Gets the TextView for tab item at index
     */
    public TextView getTextViewForItemAt(int index){
        LinearLayout ll = this.getViewForItemAt(index);
        return  (ll != null) ? (TextView)ll.getChildAt(1) : null;       
    }
    
    /**
     * Gets the LinearLayout container for tab item at index
     */
    public LinearLayout getViewForItemAt(int index){
        LinearLayout result = null;
        
        if(this.mTabStrip.getChildCount() > index){
            result = (LinearLayout)this.mTabStrip.getChildAt(index);
        }
        
        return result;
    }

    /**
     * Gets the number of realized tabs.
     */
    public int getItemCount(){
        return this.mTabStrip.getChildCount();
    }

    /**
     * Create a default view to be used for tabs.
     */
    protected View createDefaultTabView(Context context, TabItemSpec tabItem) {
        float density = getResources().getDisplayMetrics().density;

        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        ll.setBackgroundResource(outValue.resourceId);

        ImageView imgView = new ImageView(context);
        imgView.setScaleType(ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams imgLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgLP.gravity = Gravity.CENTER;
        imgView.setLayoutParams(imgLP);

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxWidth((int) (ITEM_TEXT_MAX_WIDTH * density));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ITEM_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(1);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.setupItem(ll, textView, imgView, tabItem);

        ll.addView(imgView);
        ll.addView(textView);
        return ll;
    }
    
    private void setupItem(LinearLayout ll, TextView textView,ImageView imgView, TabItemSpec tabItem){
        float density = getResources().getDisplayMetrics().density;
        
        if (tabItem.iconId != 0) {
            imgView.setImageResource(tabItem.iconId);
            imgView.setVisibility(VISIBLE);
        } else if (tabItem.iconDrawable != null) {
            imgView.setImageDrawable(tabItem.iconDrawable);
            imgView.setVisibility(VISIBLE);
        } else {
            imgView.setVisibility(GONE);
        }

        if (tabItem.title != null && !tabItem.title.isEmpty()) {
            textView.setText(tabItem.title);
            textView.setVisibility(VISIBLE);
        } else {
            textView.setVisibility(GONE);
        }

        if (tabItem.backgroundColor != 0) {
            ll.setBackgroundColor(tabItem.backgroundColor);
        }

        ll.setMinimumHeight((int) (BOTTOM_NAV_HEIGHT * density));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll.getLayoutParams();
        lp.width = 0;
        lp.weight = 1;
    }

    public void onTap(int position) {
        // to be overridden in JS
    }

    public void onSelectedPositionChange(int position, int prevPosition) {
        // to be overridden in JS
    }

    private void populateTabStrip() {
        final OnClickListener tabClickListener = new TabClickListener();

        if (this.mTabItems != null) {
            int count = this.mTabItems.length < 5 ? this.mTabItems.length : 5;
            for (int i = 0; i < count; i++) {
                View tabView = null;

                TabItemSpec tabItem;
                tabItem = this.mTabItems[i];
                tabView = createDefaultTabView(getContext(), tabItem);
                tabView.setOnClickListener(tabClickListener);

                String desc = mContentDescriptions.get(i, null);
                if (desc != null) {
                    tabView.setContentDescription(desc);
                }

                mTabStrip.addView(tabView);
            }
            int tabTextColor = mTabStrip.getTabTextColor();
            mTabStrip.setTabTextColor(Color.argb(100, Color.red(tabTextColor), Color.green(tabTextColor), Color.blue(tabTextColor)));
            mTabStrip.setSelectedTabTextColor(Color.argb(255, Color.red(tabTextColor), Color.green(tabTextColor), Color.blue(tabTextColor)));
        }
    }

    public void setSelectedPosition(int position) {
        int prevPosition = mTabStrip.getSelectedPosition();
        if (prevPosition == position) {
            return;
        }

        mTabStrip.setSelectedPosition(position);
        onSelectedPositionChange(position, prevPosition);
    }

    public void setContentDescription(int i, String desc) {
        mContentDescriptions.put(i, desc);
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
             for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                 if (v == mTabStrip.getChildAt(i)) {
                     onTap(i);
                     setSelectedPosition(i);
                     return;
                 }
             }
        }
    }
}