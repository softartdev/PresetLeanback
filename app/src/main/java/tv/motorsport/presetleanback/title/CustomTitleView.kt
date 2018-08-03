/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package tv.motorsport.presetleanback.title

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.SearchOrbView
import android.support.v17.leanback.widget.TitleViewAdapter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_title_view.view.*
import tv.motorsport.presetleanback.R

/**
 * Title view for a leanback fragment.
 */
class CustomTitleView constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr), TitleViewAdapter.Provider {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.browseTitleViewStyle)
    constructor(context: Context) : this(context, null)

    init {
        clipToPadding = false
        clipChildren = false
    }

    private val customTitleViewAdapter: CustomTitleViewAdapter by lazy {
        val rootView = LayoutInflater.from(context).inflate(R.layout.custom_title_view, this)
        CustomTitleViewAdapter(rootView.title_orb, rootView.title_text, rootView.title_badge)
    }

    override fun getTitleViewAdapter(): TitleViewAdapter = customTitleViewAdapter

    private class CustomTitleViewAdapter(
            private val searchOrbView: SearchOrbView,
            private val titleView: TextView,
            private val badgeView: ImageView
    ) : TitleViewAdapter() {
        private var mFlags = FULL_VIEW_VISIBLE
        private var mHasSearchListener = false

        /**
         * Returns the view for the search affordance.
         */
        override fun getSearchAffordanceView(): View = searchOrbView

        /**
         * Sets the listener to be called when the search affordance is clicked.
         */
        override fun setOnSearchClickedListener(listener: View.OnClickListener?) {
            mHasSearchListener = listener != null
            searchOrbView.setOnOrbClickedListener(listener)
            updateSearchOrbViewVisiblity()
        }

        /**
         * Enables or disables any view animations.
         */
        override fun setAnimationEnabled(enable: Boolean) {
            searchOrbView.enableOrbColorAnimation(enable && searchOrbView.hasFocus())
        }

        /**
         * Returns the badge drawable.
         */
        override fun getBadgeDrawable(): Drawable = badgeView.drawable

        /**
         * Returns the [SearchOrbView.Colors] used to draw the search affordance.
         */
        override fun getSearchAffordanceColors(): SearchOrbView.Colors = searchOrbView.orbColors

        /**
         * Returns the title text.
         */
        override fun getTitle(): CharSequence = titleView.text

        /**
         * Sets the badge drawable.
         * If non-null, the drawable is displayed instead of the title text.
         */
        override fun setBadgeDrawable(drawable: Drawable?) {
            badgeView.setImageDrawable(drawable)
            updateBadgeVisibility()
        }

        /**
         * Sets the [SearchOrbView.Colors] used to draw the search affordance.
         */
        override fun setSearchAffordanceColors(colors: SearchOrbView.Colors?) {
            searchOrbView.orbColors = colors
        }

        /**
         * Sets the title text.
         */
        override fun setTitle(titleText: CharSequence?) {
            titleView.text = titleText
            updateBadgeVisibility()
        }

        /**
         * Based on the flag, it updates the visibility of the individual components -
         * BadgeView, TextView and SearchView.
         *
         * @param flags integer representing the visibility of TitleView components.
         * @see TitleViewAdapter.SEARCH_VIEW_VISIBLE
         *
         * @see TitleViewAdapter.BRANDING_VIEW_VISIBLE
         *
         * @see TitleViewAdapter.FULL_VIEW_VISIBLE
         */
        override fun updateComponentsVisibility(flags: Int) {
            mFlags = flags
            if (flags and TitleViewAdapter.BRANDING_VIEW_VISIBLE == TitleViewAdapter.BRANDING_VIEW_VISIBLE) {
                updateBadgeVisibility()
            } else {
                badgeView.visibility = View.GONE
                titleView.visibility = View.GONE
            }
            updateSearchOrbViewVisiblity()
        }

        private fun updateSearchOrbViewVisiblity() {
            val visibility = if (mHasSearchListener && mFlags and SEARCH_VIEW_VISIBLE == SEARCH_VIEW_VISIBLE) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            searchOrbView.visibility = visibility
        }

        private fun updateBadgeVisibility() {
            val drawable = badgeView.drawable
            if (drawable != null) {
                badgeView.visibility = View.VISIBLE
                titleView.visibility = View.GONE
            } else {
                badgeView.visibility = View.GONE
                titleView.visibility = View.VISIBLE
            }
        }
    }
}
