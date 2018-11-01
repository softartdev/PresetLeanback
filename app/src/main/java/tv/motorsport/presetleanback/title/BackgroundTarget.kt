package tv.motorsport.presetleanback.title

import android.graphics.drawable.Drawable
import android.support.v17.leanback.app.BackgroundManager
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.util.Util

class BackgroundTarget(
        private val backgroundManager: BackgroundManager?,
        private var width: Int = Target.SIZE_ORIGINAL,
        private var height: Int = Target.SIZE_ORIGINAL,
        private var request: Request? = null
) : Target<Drawable> {

    init {
        if (!Util.isValidDimensions(width, height)) {
            throw IllegalArgumentException("Width and height must both be > 0 " +
                    "or Target#SIZE_ORIGINAL, but given width: $width and height: $height, " +
                    "either provide dimensions in the constructor or call override()")
        }
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        backgroundManager?.drawable = resource
    }

    override fun setRequest(request: Request?) {
        this@BackgroundTarget.request = request
    }

    override fun getRequest(): Request? = request

    override fun getSize(cb: SizeReadyCallback) = cb.onSizeReady(width, height)

    override fun onLoadStarted(placeholder: Drawable?) {
        placeholder?.let { backgroundManager?.drawable = it }
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        errorDrawable?.let { backgroundManager?.drawable = it }
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        placeholder?.let { backgroundManager?.drawable = it }
    }

    override fun removeCallback(cb: SizeReadyCallback) {
        // Do nothing, we never retain a reference to the callback.
    }

    override fun onStop() {
        // Do nothing.
    }

    override fun onStart() {
        // Do nothing.
    }

    override fun onDestroy() {
        // Do nothing.
    }
}